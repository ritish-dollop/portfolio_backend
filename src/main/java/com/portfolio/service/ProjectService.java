package com.portfolio.service;

import com.portfolio.dto.request.CreateProjectRequest;
import com.portfolio.dto.request.UpdateProjectRequest;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.dto.response.ProjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    PagedResponse<ProjectResponse> getPublishedProjects(Pageable pageable);

    ProjectResponse getPublishedProjectBySlug(String slug);

    List<ProjectResponse> getFeaturedProjects();

    PagedResponse<ProjectResponse> getAllProjectsForAdmin(Pageable pageable);

    ProjectResponse getProjectById(String id);

    ProjectResponse createProject(CreateProjectRequest request);

    ProjectResponse updateProject(String id, UpdateProjectRequest request);

    void deleteProject(String id);

    ProjectResponse setPublishStatus(String id, boolean published);
}
