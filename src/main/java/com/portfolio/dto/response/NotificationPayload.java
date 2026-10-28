package com.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayload {

    private String id;
    private String type;            // e.g., "NEW_CONTACT_MESSAGE", "SYSTEM", "UNREAD_COUNT_UPDATE"
    private String title;           // e.g., "New Contact Message"
    private String message;         // e.g., "Jane Doe sent you a message"
    private Object data;            // Message details or custom payload
    private long unreadCount;       // Total unread contact messages count

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
