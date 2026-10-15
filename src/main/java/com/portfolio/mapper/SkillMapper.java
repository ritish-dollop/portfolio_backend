package com.portfolio.mapper;

import com.portfolio.dto.request.CreateSkillRequest;
import com.portfolio.dto.request.UpdateSkillRequest;
import com.portfolio.dto.response.SkillResponse;
import com.portfolio.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillResponse toResponse(Skill entity) {
        if (entity == null) {
            return null;
        }
        return SkillResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .proficiencyLevel(entity.getProficiencyLevel())
                .iconUrl(entity.getIconUrl())
                .displayOrder(entity.getDisplayOrder())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Skill toEntity(CreateSkillRequest request) {
        if (request == null) {
            return null;
        }
        return Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .proficiencyLevel(request.getProficiencyLevel())
                .iconUrl(request.getIconUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .active(request.isActive())
                .build();
    }

    public void updateEntity(Skill entity, UpdateSkillRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
        entity.setCategory(request.getCategory());
        entity.setProficiencyLevel(request.getProficiencyLevel());
        entity.setIconUrl(request.getIconUrl());
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        entity.setActive(request.isActive());
    }
}
