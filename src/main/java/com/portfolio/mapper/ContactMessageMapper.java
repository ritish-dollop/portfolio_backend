package com.portfolio.mapper;

import com.portfolio.dto.request.ContactMessageRequest;
import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.entity.ContactMessage;
import org.springframework.stereotype.Component;

@Component
public class ContactMessageMapper {

    public ContactMessageResponse toResponse(ContactMessage entity) {
        if (entity == null) {
            return null;
        }
        return ContactMessageResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .subject(entity.getSubject())
                .message(entity.getMessage())
                .read(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ContactMessage toEntity(ContactMessageRequest request) {
        if (request == null) {
            return null;
        }
        return ContactMessage.builder()
                .name(request.getName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .subject(request.getSubject().trim())
                .message(request.getMessage().trim())
                .read(false)
                .build();
    }
}
