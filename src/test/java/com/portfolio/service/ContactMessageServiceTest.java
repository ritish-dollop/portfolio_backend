package com.portfolio.service;

import com.portfolio.dto.request.ContactMessageRequest;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.entity.ContactMessage;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ContactMessageMapper;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.service.NotificationSocketService;
import com.portfolio.service.impl.ContactMessageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @Mock
    private NotificationSocketService notificationSocketService;

    @Spy
    private ContactMessageMapper contactMessageMapper = new ContactMessageMapper();

    @InjectMocks
    private ContactMessageServiceImpl contactMessageService;

    @Test
    @DisplayName("Should submit contact message successfully")
    void submitMessage_Success() {
        ContactMessageRequest request = ContactMessageRequest.builder()
                .name("Alice Smith")
                .email("alice@example.com")
                .subject("Project Inquiry")
                .message("Hello, I would like to collaborate on a project.")
                .build();

        when(contactMessageRepository.save(any(ContactMessage.class))).thenAnswer(invocation -> {
            ContactMessage msg = invocation.getArgument(0);
            msg.setId("uuid-msg-1");
            msg.setCreatedAt(LocalDateTime.now());
            return msg;
        });

        ContactMessageResponse response = contactMessageService.submitMessage(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Alice Smith");
        assertThat(response.getEmail()).isEqualTo("alice@example.com");
        assertThat(response.isRead()).isFalse();
        verify(contactMessageRepository).save(any(ContactMessage.class));
        verify(notificationSocketService).sendContactMessageNotification(any(ContactMessageResponse.class), any(Long.class));
    }

    @Test
    @DisplayName("Should mark message as read")
    void markAsRead_Success() {
        ContactMessage message = ContactMessage.builder()
                .id("uuid-msg-1")
                .name("Alice")
                .email("alice@example.com")
                .subject("Test")
                .message("Test content")
                .read(false)
                .build();

        when(contactMessageRepository.findById("uuid-msg-1")).thenReturn(Optional.of(message));
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(message);

        ContactMessageResponse response = contactMessageService.markAsRead("uuid-msg-1", true);

        assertThat(response).isNotNull();
        assertThat(message.isRead()).isTrue();
        verify(contactMessageRepository).save(message);
        verify(notificationSocketService).broadcastUnreadCount(any(Long.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when message not found")
    void getMessageById_NotFound_ThrowsException() {
        when(contactMessageRepository.findById("non-existent-uuid")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactMessageService.getMessageById("non-existent-uuid"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
