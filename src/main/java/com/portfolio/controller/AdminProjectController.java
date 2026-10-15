package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.CreateProjectRequest;
import com.portfolio.dto.request.UpdateProjectRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.dto.response.ProjectResponse;
import com.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ADMIN_PROJECTS)
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;

    @GetMapping(ApiConstants.GET_ALL_PROJECTS)
    public ResponseEntity<ApiResponse<PagedResponse<ProjectResponse>>> getAllProjects(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY_DISPLAY_ORDER) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION_ASC) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.DEFAULT_SORT_DIRECTION_DESC)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<ProjectResponse> response = projectService.getAllProjectsForAdmin(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(ApiConstants.GET_PROJECT_BY_ID)
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable String id) {
        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping(ApiConstants.CREATE_PROJECT)
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse created = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_PROJECT_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_PROJECT)
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectRequest request) {
        ProjectResponse updated = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_PROJECT_UPDATED_SUCCESS, updated));
    }

    @PatchMapping(ApiConstants.SET_PUBLISH_STATUS)
    public ResponseEntity<ApiResponse<ProjectResponse>> setPublishStatus(
            @PathVariable String id,
            @RequestParam boolean published) {
        ProjectResponse updated = projectService.setPublishStatus(id, published);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_PROJECT_PUBLISH_STATUS_UPDATED, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_PROJECT)
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
