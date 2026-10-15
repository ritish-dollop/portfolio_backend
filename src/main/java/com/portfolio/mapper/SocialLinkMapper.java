package com.portfolio.mapper;

import com.portfolio.dto.request.SocialLinkRequest;
import com.portfolio.dto.response.SocialLinkResponse;
import com.portfolio.entity.SocialLink;
import org.springframework.stereotype.Component;

@Component
public class SocialLinkMapper {

    public SocialLinkResponse toResponse(SocialLink entity) {
        if (entity == null) {
            return null;
        }
        return SocialLinkResponse.builder()
                .id(entity.getId())
                .platform(entity.getPlatform())
                .label(entity.getLabel())
                .url(entity.getUrl())
                .icon(entity.getIcon())
                .displayOrder(entity.getDisplayOrder())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public SocialLink toEntity(SocialLinkRequest request) {
        if (request == null) {
            return null;
        }
        return SocialLink.builder()
                .platform(request.getPlatform())
                .label(request.getLabel())
                .url(request.getUrl())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .active(request.isActive())
                .build();
    }

    public void updateEntity(SocialLink entity, SocialLinkRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setPlatform(request.getPlatform());
        entity.setLabel(request.getLabel());
        entity.setUrl(request.getUrl());
        entity.setIcon(request.getIcon());
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        entity.setActive(request.isActive());
    }
}
