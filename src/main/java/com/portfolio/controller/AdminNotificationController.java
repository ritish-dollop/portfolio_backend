package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.request.BroadcastNotificationRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.NotificationPayload;
import com.portfolio.service.NotificationSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.ADMIN_NOTIFICATIONS)
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationSocketService notificationSocketService;

    @Value("${app.socketio.port:9092}")
    private int socketPort;

    @Value("${app.socketio.host:0.0.0.0}")
    private String socketHost;

    @GetMapping(ApiConstants.GET_SOCKET_STATUS)
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSocketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("running", notificationSocketService.isServerRunning());
        status.put("connectedClients", notificationSocketService.getConnectedClientsCount());
        status.put("port", socketPort);
        status.put("host", socketHost);
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @PostMapping(ApiConstants.BROADCAST_NOTIFICATION)
    public ResponseEntity<ApiResponse<NotificationPayload>> broadcastNotification(
            @Valid @RequestBody BroadcastNotificationRequest request) {

        String type = (request.getType() != null && !request.getType().isBlank())
                ? request.getType()
                : "SYSTEM_NOTIFICATION";

        NotificationPayload payload = NotificationPayload.builder()
                .id(UUID.randomUUID().toString())
                .type(type)
                .title(request.getTitle())
                .message(request.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        if (request.isAdminOnly()) {
            notificationSocketService.sendNotificationToAdmin(payload);
        } else {
            notificationSocketService.sendNotificationToAll(payload);
        }

        return ResponseEntity.ok(ApiResponse.success("Notification broadcasted successfully", payload));
    }

    @PostMapping(ApiConstants.SEED_NOTIFICATIONS)
    public ResponseEntity<ApiResponse<List<ContactMessageResponse>>> seedNotifications() {
        List<ContactMessageResponse> seeded = notificationSocketService.seedSampleNotifications();
        return ResponseEntity.ok(ApiResponse.success("Successfully seeded sample notifications and broadcasted over Socket.IO", seeded));
    }

    @DeleteMapping(ApiConstants.CLEAR_ALL_NOTIFICATIONS)
    public ResponseEntity<ApiResponse<Void>> clearAllNotifications() {
        notificationSocketService.clearAllMessages();
        return ResponseEntity.ok(ApiResponse.success("All messages and notifications cleared successfully", null));
    }
}
