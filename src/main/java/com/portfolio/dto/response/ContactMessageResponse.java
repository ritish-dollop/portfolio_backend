package com.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageResponse {

    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
