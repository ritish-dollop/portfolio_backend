package com.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastNotificationRequest {

    @NotBlank(message = "Notification title is required")
    private String title;

    @NotBlank(message = "Notification message is required")
    private String message;

    private String type;

    private boolean adminOnly;
}
