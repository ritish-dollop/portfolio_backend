package com.portfolio.mapper;

import com.portfolio.dto.request.AdminUserRequest;
import com.portfolio.dto.response.AdminUserResponse;
import com.portfolio.entity.AdminUser;
import com.portfolio.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper {

    public AdminUserResponse toResponse(AdminUser entity) {
        if (entity == null) {
            return null;
        }
        return AdminUserResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .enabled(entity.isEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public AdminUser toEntity(AdminUserRequest request, String encodedPassword) {
        if (request == null) {
            return null;
        }
        return AdminUser.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase().trim())
                .password(encodedPassword)
                .role(request.getRole() != null ? request.getRole() : Role.ADMIN)
                .enabled(request.isEnabled())
                .build();
    }

    public void updateEntity(AdminUser entity, AdminUserRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setFullName(request.getFullName());
        entity.setEmail(request.getEmail().toLowerCase().trim());
        if (request.getRole() != null) {
            entity.setRole(request.getRole());
        }
        entity.setEnabled(request.isEnabled());
    }
}
