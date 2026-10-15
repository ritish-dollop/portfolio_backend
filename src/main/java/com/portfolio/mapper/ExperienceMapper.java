package com.portfolio.mapper;

import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.dto.response.ExperienceResponse;
import com.portfolio.entity.Experience;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ExperienceMapper {

    public ExperienceResponse toResponse(Experience entity) {
        if (entity == null) {
            return null;
        }
        return ExperienceResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .jobTitle(entity.getJobTitle())
                .employmentType(entity.getEmploymentType())
                .location(entity.getLocation())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .currentlyWorking(entity.isCurrentlyWorking())
                .responsibilities(entity.getResponsibilities() != null ? new ArrayList<>(entity.getResponsibilities()) : new ArrayList<>())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Experience toEntity(ExperienceRequest request) {
        if (request == null) {
            return null;
        }
        return Experience.builder()
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .employmentType(request.getEmploymentType())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.isCurrentlyWorking() ? null : request.getEndDate())
                .currentlyWorking(request.isCurrentlyWorking())
                .responsibilities(request.getResponsibilities() != null ? new ArrayList<>(request.getResponsibilities()) : new ArrayList<>())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
    }

    public void updateEntity(Experience entity, ExperienceRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setCompanyName(request.getCompanyName());
        entity.setJobTitle(request.getJobTitle());
        entity.setEmploymentType(request.getEmploymentType());
        entity.setLocation(request.getLocation());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.isCurrentlyWorking() ? null : request.getEndDate());
        entity.setCurrentlyWorking(request.isCurrentlyWorking());
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getResponsibilities() != null) {
            entity.getResponsibilities().clear();
            entity.getResponsibilities().addAll(request.getResponsibilities());
        }
    }
}
