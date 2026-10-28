package com.portfolio.config;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portfolio.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

    @Value("${app.socketio.host:0.0.0.0}")
    private String host;

    @Value("${app.socketio.port:9092}")
    private int port;

    @Value("${app.socketio.boss-count:1}")
    private int bossCount;

    @Value("${app.socketio.work-count:100}")
    private int workCount;

    @Value("${app.socketio.allow-custom-requests:true}")
    private boolean allowCustomRequests;

    @Value("${app.socketio.upgrade-timeout:1000000}")
    private int upgradeTimeout;

    @Value("${app.socketio.ping-timeout:60000}")
    private int pingTimeout;

    @Value("${app.socketio.ping-interval:25000}")
    private int pingInterval;

    @Value("${app.socketio.max-frame-payload-length:1048576}")
    private int maxFramePayloadLength;

    @Value("${app.socketio.max-http-content-length:1048576}")
    private int maxHttpContentLength;

    private final JwtTokenProvider jwtTokenProvider;

    public SocketIOConfig(@Autowired(required = false) JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean(destroyMethod = "stop")
    @ConditionalOnProperty(name = "app.socketio.enabled", havingValue = "true", matchIfMissing = true)
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        config.setMaxFramePayloadLength(maxFramePayloadLength);
        config.setMaxHttpContentLength(maxHttpContentLength);

        // Allow any origin for CORS flexibility (supports both local dev and production client origins)
        config.setOrigin(null);

        // Support Java 8/21 LocalDateTime and JSON serialization
        config.setJsonSupport(new JacksonJsonSupport(new JavaTimeModule()));

        // Low-level socket options
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        config.setSocketConfig(socketConfig);

        // Handshake authorization logic (JWT support via query param or Authorization header)
        config.setAuthorizationListener(data -> {
            String token = data.getSingleUrlParam("token");
            if (token == null || token.isBlank()) {
                String authHeader = data.getHttpHeaders().get("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }
            }

            Map<String, Object> storeParams = new HashMap<>();
            if (token != null && !token.isBlank() && jwtTokenProvider != null) {
                try {
                    if (jwtTokenProvider.validateAccessToken(token)) {
                        String username = jwtTokenProvider.getUsernameFromJwt(token);
                        storeParams.put("authenticated", true);
                        storeParams.put("username", username);
                        storeParams.put("role", "ADMIN");
                        log.info("Socket.IO client handshake authenticated for user: {}", username);
                        return new AuthorizationResult(true, storeParams);
                    } else {
                        log.warn("Socket.IO handshake with invalid or expired token");
                    }
                } catch (Exception e) {
                    log.warn("Socket.IO handshake token validation error: {}", e.getMessage());
                }
            }

            // Allow guest connection for public events or late authentication
            storeParams.put("authenticated", false);
            storeParams.put("role", "GUEST");
            return new AuthorizationResult(true, storeParams);
        });

        log.info("Configured SocketIOServer on {}:{}", host, port);
        return new SocketIOServer(config);
    }
}
