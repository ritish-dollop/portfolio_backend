package com.portfolio.mapper;

import com.portfolio.dto.request.SiteSettingRequest;
import com.portfolio.dto.response.SiteSettingResponse;
import com.portfolio.entity.SiteSetting;
import org.springframework.stereotype.Component;

@Component
public class SiteSettingMapper {

    public SiteSettingResponse toResponse(SiteSetting entity) {
        if (entity == null) {
            return null;
        }
        return SiteSettingResponse.builder()
                .id(entity.getId())
                .settingKey(entity.getSettingKey())
                .settingValue(entity.getSettingValue())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public SiteSetting toEntity(SiteSettingRequest request) {
        if (request == null) {
            return null;
        }
        return SiteSetting.builder()
                .settingKey(request.getSettingKey().trim())
                .settingValue(request.getSettingValue())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(SiteSetting entity, SiteSettingRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setSettingKey(request.getSettingKey().trim());
        entity.setSettingValue(request.getSettingValue());
        entity.setDescription(request.getDescription());
    }
}
