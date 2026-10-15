package com.portfolio.mapper;

import com.portfolio.dto.request.CreateProjectRequest;
import com.portfolio.dto.request.UpdateProjectRequest;
import com.portfolio.dto.response.ProjectResponse;
import com.portfolio.entity.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project entity) {
        if (entity == null) {
            return null;
        }
        return ProjectResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .shortDescription(entity.getShortDescription())
                .fullDescription(entity.getFullDescription())
                .thumbnailUrl(entity.getThumbnailUrl())
                .githubUrl(entity.getGithubUrl())
                .liveDemoUrl(entity.getLiveDemoUrl())
                .featured(entity.isFeatured())
                .published(entity.isPublished())
                .displayOrder(entity.getDisplayOrder())
                .technologies(entity.getTechnologies() != null ? new ArrayList<>(entity.getTechnologies()) : new ArrayList<>())
                .imageUrls(entity.getImageUrls() != null ? new ArrayList<>(entity.getImageUrls()) : new ArrayList<>())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Project toEntity(CreateProjectRequest request, String resolvedSlug) {
        if (request == null) {
            return null;
        }
        return Project.builder()
                .title(request.getTitle())
                .slug(resolvedSlug)
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .githubUrl(request.getGithubUrl())
                .liveDemoUrl(request.getLiveDemoUrl())
                .featured(request.isFeatured())
                .published(request.isPublished())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .technologies(request.getTechnologies() != null ? new ArrayList<>(request.getTechnologies()) : new ArrayList<>())
                .imageUrls(request.getImageUrls() != null ? new ArrayList<>(request.getImageUrls()) : new ArrayList<>())
                .build();
    }

    public void updateEntity(Project entity, UpdateProjectRequest request, String resolvedSlug) {
        if (entity == null || request == null) {
            return;
        }
        entity.setTitle(request.getTitle());
        entity.setSlug(resolvedSlug);
        entity.setShortDescription(request.getShortDescription());
        entity.setFullDescription(request.getFullDescription());
        entity.setThumbnailUrl(request.getThumbnailUrl());
        entity.setGithubUrl(request.getGithubUrl());
        entity.setLiveDemoUrl(request.getLiveDemoUrl());
        entity.setFeatured(request.isFeatured());
        entity.setPublished(request.isPublished());
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getTechnologies() != null) {
            entity.getTechnologies().clear();
            entity.getTechnologies().addAll(request.getTechnologies());
        }
        if (request.getImageUrls() != null) {
            entity.getImageUrls().clear();
            entity.getImageUrls().addAll(request.getImageUrls());
        }
    }
}
