package com.portfolio.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.NotificationPayload;
import com.portfolio.entity.ContactMessage;
import com.portfolio.mapper.ContactMessageMapper;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.security.JwtTokenProvider;
import com.portfolio.service.NotificationSocketService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NotificationSocketServiceImpl implements NotificationSocketService {

    public static final String ROOM_ADMIN = "admin";
    public static final String EVENT_NOTIFICATION = "notification";
    public static final String EVENT_ADMIN_NOTIFICATION = "admin_notification";
    public static final String EVENT_NEW_CONTACT_MESSAGE = "new_contact_message";
    public static final String EVENT_UNREAD_COUNT = "unread_count";
    public static final String EVENT_CONNECTION_STATUS = "connection_status";
    public static final String EVENT_AUTH_SUCCESS = "auth_success";
    public static final String EVENT_AUTH_ERROR = "auth_error";
    public static final String EVENT_PONG = "pong";

    private final SocketIOServer socketIOServer;
    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public NotificationSocketServiceImpl(
            @Autowired(required = false) SocketIOServer socketIOServer,
            ContactMessageRepository contactMessageRepository,
            ContactMessageMapper contactMessageMapper,
            @Autowired(required = false) JwtTokenProvider jwtTokenProvider) {
        this.socketIOServer = socketIOServer;
        this.contactMessageRepository = contactMessageRepository;
        this.contactMessageMapper = contactMessageMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostConstruct
    public void registerListeners() {
        if (socketIOServer == null) {
            log.info("Socket.IO server bean is not configured or disabled; skipping listener registration.");
            return;
        }

        // Connection event listener
        socketIOServer.addConnectListener(client -> {
            log.info("Socket.IO client connected: session={}, remote={}",
                    client.getSessionId(), client.getRemoteAddress());

            Boolean authenticated = (Boolean) client.get("authenticated");
            if (Boolean.TRUE.equals(authenticated)) {
                client.joinRoom(ROOM_ADMIN);
                log.info("Client session {} auto-joined '{}' room after handshake auth",
                        client.getSessionId(), ROOM_ADMIN);
            }

            Map<String, Object> welcome = new HashMap<>();
            welcome.put("status", "connected");
            welcome.put("sessionId", client.getSessionId().toString());
            welcome.put("authenticated", Boolean.TRUE.equals(authenticated));
            welcome.put("timestamp", LocalDateTime.now().toString());
            client.sendEvent(EVENT_CONNECTION_STATUS, welcome);
        });

        // Disconnection event listener
        socketIOServer.addDisconnectListener(client -> {
            log.info("Socket.IO client disconnected: session={}", client.getSessionId());
        });

        // Post-connect authentication event listener
        socketIOServer.addEventListener("authenticate", Map.class, (client, data, ackSender) -> {
            Object tokenObj = data != null ? data.get("token") : null;
            String token = tokenObj != null ? tokenObj.toString() : null;

            if (token != null && jwtTokenProvider != null && jwtTokenProvider.validateAccessToken(token)) {
                String username = jwtTokenProvider.getUsernameFromJwt(token);
                client.set("authenticated", true);
                client.set("username", username);
                client.joinRoom(ROOM_ADMIN);
                log.info("Client session {} successfully authenticated as user '{}'", client.getSessionId(), username);

                Map<String, Object> authResponse = new HashMap<>();
                authResponse.put("authenticated", true);
                authResponse.put("username", username);
                authResponse.put("room", ROOM_ADMIN);
                client.sendEvent(EVENT_AUTH_SUCCESS, authResponse);
            } else {
                log.warn("Client session {} failed post-connect authentication", client.getSessionId());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("authenticated", false);
                errorResponse.put("message", "Invalid or expired JWT token");
                client.sendEvent(EVENT_AUTH_ERROR, errorResponse);
            }
        });

        // Join admin room event listener
        socketIOServer.addEventListener("join_admin", Void.class, (client, data, ackSender) -> {
            client.joinRoom(ROOM_ADMIN);
            log.info("Client session {} joined '{}' room", client.getSessionId(), ROOM_ADMIN);
        });

        // Leave admin room event listener
        socketIOServer.addEventListener("leave_admin", Void.class, (client, data, ackSender) -> {
            client.leaveRoom(ROOM_ADMIN);
            log.info("Client session {} left '{}' room", client.getSessionId(), ROOM_ADMIN);
        });

        // Client requesting latest unread messages count
        socketIOServer.addEventListener("request_unread_count", Void.class, (client, data, ackSender) -> {
            long count = contactMessageRepository.countByReadFalse();
            Map<String, Object> response = new HashMap<>();
            response.put("unreadCount", count);
            client.sendEvent(EVENT_UNREAD_COUNT, response);
        });

        // Health check ping/pong
        socketIOServer.addEventListener("ping", String.class, (client, data, ackSender) -> {
            Map<String, Object> pongData = new HashMap<>();
            pongData.put("response", "pong");
            pongData.put("serverTime", LocalDateTime.now().toString());
            client.sendEvent(EVENT_PONG, pongData);
        });

        log.info("Registered Socket.IO event listeners successfully.");
    }

    @Override
    public void sendNotificationToAll(NotificationPayload notification) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped sendNotificationToAll");
            return;
        }
        socketIOServer.getBroadcastOperations().sendEvent(EVENT_NOTIFICATION, notification);
        log.info("Broadcasted notification to all clients: type={}, title={}",
                notification.getType(), notification.getTitle());
    }

    @Override
    public void sendNotificationToAdmin(NotificationPayload notification) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped sendNotificationToAdmin");
            return;
        }
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(EVENT_ADMIN_NOTIFICATION, notification);
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(EVENT_NOTIFICATION, notification);
        log.info("Broadcasted admin notification to '{}' room: type={}, title={}",
                ROOM_ADMIN, notification.getType(), notification.getTitle());
    }

    @Override
    public void sendContactMessageNotification(ContactMessageResponse message, long unreadCount) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped sendContactMessageNotification");
            return;
        }

        String snippet = message.getSubject() != null && !message.getSubject().isBlank()
                ? message.getSubject()
                : (message.getMessage() != null && message.getMessage().length() > 60
                ? message.getMessage().substring(0, 60) + "..."
                : message.getMessage());

        NotificationPayload payload = NotificationPayload.builder()
                .id(message.getId())
                .type("NEW_CONTACT_MESSAGE")
                .title("New Message from " + message.getName())
                .message(snippet)
                .data(message)
                .unreadCount(unreadCount)
                .timestamp(message.getCreatedAt() != null ? message.getCreatedAt() : LocalDateTime.now())
                .build();

        // Send to admin room and general room operations
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(EVENT_NEW_CONTACT_MESSAGE, payload);
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(EVENT_ADMIN_NOTIFICATION, payload);
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(EVENT_NOTIFICATION, payload);

        // Also broadcast unread count to all connected clients
        broadcastUnreadCount(unreadCount);

        log.info("Dispatched new contact message notification: messageId={}, sender={}",
                message.getId(), message.getEmail());
    }

    @Override
    public void broadcastUnreadCount(long count) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped broadcastUnreadCount");
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("unreadCount", count);
        socketIOServer.getBroadcastOperations().sendEvent(EVENT_UNREAD_COUNT, data);
        log.info("Broadcasted unread count update: count={}", count);
    }

    @Override
    public void sendCustomEvent(String eventName, Object data) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped sendCustomEvent");
            return;
        }
        socketIOServer.getBroadcastOperations().sendEvent(eventName, data);
        log.info("Sent custom Socket.IO event '{}' to all clients", eventName);
    }

    @Override
    public void sendCustomEventToAdmin(String eventName, Object data) {
        if (!isServerRunning()) {
            log.debug("Socket.IO server not running; skipped sendCustomEventToAdmin");
            return;
        }
        socketIOServer.getRoomOperations(ROOM_ADMIN).sendEvent(eventName, data);
        log.info("Sent custom Socket.IO event '{}' to admin room", eventName);
    }

    @Override
    public int getConnectedClientsCount() {
        if (!isServerRunning()) {
            return 0;
        }
        return socketIOServer.getAllClients().size();
    }

    @Override
    public boolean isServerRunning() {
        return socketIOServer != null;
    }

    @Override
    @Transactional
    public List<ContactMessageResponse> seedSampleNotifications() {
        List<ContactMessage> samples = List.of(
                ContactMessage.builder()
                        .name("Sarah Jenkins")
                        .email("sarah.jenkins@techventures.io")
                        .subject("Contract Inquiry: High-Performance Distributed Systems")
                        .message("Hi! We came across your portfolio and are deeply impressed with your architecture and real-time systems work. We have a 6-month contract for a high-growth fintech application. Would you be open for an introductory call this week?")
                        .read(false)
                        .createdAt(LocalDateTime.now().minusMinutes(18))
                        .build(),
                ContactMessage.builder()
                        .name("Alex Rivera")
                        .email("alex.rivera@globalscale.co")
                        .subject("Speaking Invitation: Global Tech Summit 2026")
                        .message("Hello! Our program committee would be thrilled to have you deliver a technical session on real-time event streaming and Socket.IO microservices at GTS 2026.")
                        .read(false)
                        .createdAt(LocalDateTime.now().minusHours(2))
                        .build(),
                ContactMessage.builder()
                        .name("Elena Rostova")
                        .email("elena@designcraft.studio")
                        .subject("Collaboration on Open Source Developer UI Library")
                        .message("Hi there! We are crafting a developer-first design system and dashboard UI. We'd love your expertise on the backend API specifications and integration contracts.")
                        .read(false)
                        .createdAt(LocalDateTime.now().minusHours(5))
                        .build(),
                ContactMessage.builder()
                        .name("David Ross")
                        .email("david.ross@cloudscale.net")
                        .subject("Consulting: Database Query Optimization & Redis Caching")
                        .message("We are reviewing our Redis cache invalidation strategy and database query indexing. We would love to book an advisory session with you.")
                        .read(true)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .build(),
                ContactMessage.builder()
                        .name("Marcus Vance")
                        .email("marcus@vancecapital.com")
                        .subject("Technical Advisory Opportunity for Portfolio Startups")
                        .message("Greetings! Vance Capital is establishing an engineering advisory board for our developer tooling companies. Your background fits our profile perfectly.")
                        .read(false)
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .build()
        );

        List<ContactMessage> savedList = contactMessageRepository.saveAll(samples);
        List<ContactMessageResponse> responses = savedList.stream()
                .map(contactMessageMapper::toResponse)
                .toList();

        long unreadCount = contactMessageRepository.countByReadFalse();
        broadcastUnreadCount(unreadCount);

        // Broadcast live socket notifications for the seeded unread items
        responses.stream()
                .filter(res -> !res.isRead())
                .forEach(res -> sendContactMessageNotification(res, unreadCount));

        log.info("Successfully seeded {} sample contact messages. Current unread count: {}",
                savedList.size(), unreadCount);

        return responses;
    }

    @Override
    @Transactional
    public void clearAllMessages() {
        contactMessageRepository.deleteAll();
        broadcastUnreadCount(0L);
        log.info("Cleared all contact messages and broadcasted zero unread count.");
    }
}
