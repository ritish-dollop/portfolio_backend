package com.portfolio.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(10)
@ConditionalOnProperty(name = "app.socketio.enabled", havingValue = "true", matchIfMissing = true)
public class SocketIOServerRunner implements CommandLineRunner, DisposableBean {

    private final SocketIOServer socketIOServer;

    public SocketIOServerRunner(@Autowired(required = false) SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void run(String... args) {
        if (socketIOServer == null) {
            log.info("SocketIOServer bean is not configured or disabled; skipping startup.");
            return;
        }

        try {
            log.info("Starting Socket.IO Server on {}:{}...",
                    socketIOServer.getConfiguration().getHostname(),
                    socketIOServer.getConfiguration().getPort());
            socketIOServer.start();
            log.info("Socket.IO Server started successfully on port {}.", socketIOServer.getConfiguration().getPort());
        } catch (Exception e) {
            log.error("Failed to start Socket.IO server: {}", e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        if (socketIOServer != null) {
            log.info("Stopping Socket.IO Server...");
            try {
                socketIOServer.stop();
                log.info("Socket.IO Server stopped successfully.");
            } catch (Exception e) {
                log.warn("Error while stopping Socket.IO server: {}", e.getMessage());
            }
        }
    }
}
