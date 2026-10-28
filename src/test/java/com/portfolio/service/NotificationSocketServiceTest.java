package com.portfolio.service;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.NotificationPayload;
import com.portfolio.entity.ContactMessage;
import com.portfolio.mapper.ContactMessageMapper;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.security.JwtTokenProvider;
import com.portfolio.service.impl.NotificationSocketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationSocketServiceTest {

    @Mock
    private SocketIOServer socketIOServer;

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ContactMessageMapper contactMessageMapper = new ContactMessageMapper();

    @Mock
    private BroadcastOperations broadcastOperations;

    private NotificationSocketServiceImpl notificationSocketService;

    @BeforeEach
    void setUp() {
        notificationSocketService = new NotificationSocketServiceImpl(
                socketIOServer,
                contactMessageRepository,
                contactMessageMapper,
                jwtTokenProvider
        );
    }

    @Test
    @DisplayName("Should send notification to all clients")
    void sendNotificationToAll_Success() {
        when(socketIOServer.getBroadcastOperations()).thenReturn(broadcastOperations);

        NotificationPayload payload = NotificationPayload.builder()
                .id("notif-1")
                .type("SYSTEM")
                .title("System Alert")
                .message("Maintenance at midnight")
                .build();

        notificationSocketService.sendNotificationToAll(payload);

        verify(broadcastOperations).sendEvent("notification", payload);
    }

    @Test
    @DisplayName("Should send contact message notification and update unread count")
    void sendContactMessageNotification_Success() {
        when(socketIOServer.getRoomOperations("admin")).thenReturn(broadcastOperations);
        when(socketIOServer.getBroadcastOperations()).thenReturn(broadcastOperations);

        ContactMessageResponse message = ContactMessageResponse.builder()
                .id("msg-1")
                .name("John Doe")
                .email("john@example.com")
                .subject("Consultation Request")
                .message("I would like to discuss a project")
                .createdAt(LocalDateTime.now())
                .build();

        notificationSocketService.sendContactMessageNotification(message, 5L);

        verify(broadcastOperations).sendEvent(eq("new_contact_message"), any(NotificationPayload.class));
        verify(broadcastOperations).sendEvent(eq("unread_count"), any(Map.class));
    }

    @Test
    @DisplayName("Should broadcast unread count update")
    void broadcastUnreadCount_Success() {
        when(socketIOServer.getBroadcastOperations()).thenReturn(broadcastOperations);

        notificationSocketService.broadcastUnreadCount(3L);

        verify(broadcastOperations).sendEvent(eq("unread_count"), any(Map.class));
    }

    @Test
    @DisplayName("Should return connected clients count")
    void getConnectedClientsCount_Success() {
        Collection<SocketIOClient> clients = Collections.singletonList(mock(SocketIOClient.class));
        when(socketIOServer.getAllClients()).thenReturn(clients);

        int count = notificationSocketService.getConnectedClientsCount();

        assertThat(count).isEqualTo(1);
        assertThat(notificationSocketService.isServerRunning()).isTrue();
    }

    @Test
    @DisplayName("Should seed sample notifications and broadcast")
    void seedSampleNotifications_Success() {
        when(contactMessageRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(contactMessageRepository.countByReadFalse()).thenReturn(4L);
        when(socketIOServer.getBroadcastOperations()).thenReturn(broadcastOperations);
        when(socketIOServer.getRoomOperations("admin")).thenReturn(broadcastOperations);

        List<ContactMessageResponse> seeded = notificationSocketService.seedSampleNotifications();

        assertThat(seeded).hasSize(5);
        verify(contactMessageRepository).saveAll(anyList());
        verify(broadcastOperations, atLeastOnce()).sendEvent(eq("unread_count"), any(Map.class));
    }

    @Test
    @DisplayName("Should clear all messages and broadcast zero unread count")
    void clearAllMessages_Success() {
        when(socketIOServer.getBroadcastOperations()).thenReturn(broadcastOperations);

        notificationSocketService.clearAllMessages();

        verify(contactMessageRepository).deleteAll();
        verify(broadcastOperations).sendEvent(eq("unread_count"), any(Map.class));
    }

    @Test
    @DisplayName("Should handle null SocketIOServer safely when disabled")
    void disabledServer_HandlesGracefully() {
        NotificationSocketServiceImpl disabledService = new NotificationSocketServiceImpl(
                null,
                contactMessageRepository,
                contactMessageMapper,
                jwtTokenProvider
        );

        assertThat(disabledService.isServerRunning()).isFalse();
        assertThat(disabledService.getConnectedClientsCount()).isEqualTo(0);

        NotificationPayload payload = NotificationPayload.builder()
                .id("1")
                .title("Test")
                .message("Test")
                .build();

        // Must not throw any NullPointerException
        disabledService.sendNotificationToAll(payload);
        disabledService.sendNotificationToAdmin(payload);
        disabledService.broadcastUnreadCount(1L);
    }
}
