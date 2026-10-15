package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.dto.response.ProjectResponse;
import com.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_PROJECTS)
@RequiredArgsConstructor
public class PublicProjectController {

    private final ProjectService projectService;

    @GetMapping(ApiConstants.GET_PUBLISHED_PROJECTS)
    public ResponseEntity<ApiResponse<PagedResponse<ProjectResponse>>> getPublishedProjects(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY_DISPLAY_ORDER) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION_ASC) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.DEFAULT_SORT_DIRECTION_DESC)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<ProjectResponse> response = projectService.getPublishedProjects(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(ApiConstants.GET_FEATURED_PROJECTS)
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getFeaturedProjects() {
        List<ProjectResponse> response = projectService.getFeaturedProjects();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(ApiConstants.GET_PROJECT_BY_SLUG)
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectBySlug(@PathVariable String slug) {
        ProjectResponse response = projectService.getPublishedProjectBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
