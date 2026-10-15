package com.portfolio.mapper;

import com.portfolio.dto.request.EducationRequest;
import com.portfolio.dto.response.EducationResponse;
import com.portfolio.entity.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public EducationResponse toResponse(Education entity) {
        if (entity == null) {
            return null;
        }
        return EducationResponse.builder()
                .id(entity.getId())
                .degree(entity.getDegree())
                .institution(entity.getInstitution())
                .fieldOfStudy(entity.getFieldOfStudy())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .grade(entity.getGrade())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Education toEntity(EducationRequest request) {
        if (request == null) {
            return null;
        }
        return Education.builder()
                .degree(request.getDegree())
                .institution(request.getInstitution())
                .fieldOfStudy(request.getFieldOfStudy())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .grade(request.getGrade())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(Education entity, EducationRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setDegree(request.getDegree());
        entity.setInstitution(request.getInstitution());
        entity.setFieldOfStudy(request.getFieldOfStudy());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setGrade(request.getGrade());
        entity.setDescription(request.getDescription());
    }
}
