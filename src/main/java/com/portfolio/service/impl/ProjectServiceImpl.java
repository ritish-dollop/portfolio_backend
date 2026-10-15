package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.CreateProjectRequest;
import com.portfolio.dto.request.UpdateProjectRequest;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.dto.response.ProjectResponse;
import com.portfolio.entity.Project;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProjectMapper;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-+|-+$)");

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_PROJECTS, key = "'published_p' + #pageable.pageNumber + '_s' + #pageable.pageSize + '_' + #pageable.sort")
    public PagedResponse<ProjectResponse> getPublishedProjects(Pageable pageable) {
        Page<Project> page = projectRepository.findAllByPublishedTrue(pageable);
        return PagedResponse.of(page.map(projectMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_PROJECTS, key = "'slug_' + #slug")
    public ProjectResponse getPublishedProjectBySlug(String slug) {
        Project project = projectRepository.findByPublishedTrueAndSlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "slug", slug));
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_PROJECTS, key = "'featured'")
    public List<ProjectResponse> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueAndPublishedTrueOrderByDisplayOrderAsc().stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectResponse> getAllProjectsForAdmin(Pageable pageable) {
        Page<Project> page = projectRepository.findAllByOrderByDisplayOrderAsc(pageable);
        return PagedResponse.of(page.map(projectMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROJECTS, allEntries = true)
    public ProjectResponse createProject(CreateProjectRequest request) {
        String slug = resolveSlug(request.getSlug(), request.getTitle());

        if (projectRepository.existsBySlugIgnoreCase(slug)) {
            throw new DuplicateResourceException("Project", "slug", slug);
        }

        Project project = projectMapper.toEntity(request, slug);
        Project saved = projectRepository.save(project);
        log.info("Created new project: {} with slug: {}", saved.getTitle(), saved.getSlug());
        return projectMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROJECTS, allEntries = true)
    public ProjectResponse updateProject(String id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        String slug = resolveSlug(request.getSlug(), request.getTitle());

        if (projectRepository.existsBySlugIgnoreCaseAndIdNot(slug, id)) {
            throw new DuplicateResourceException("Project", "slug", slug);
        }

        projectMapper.updateEntity(project, request, slug);
        Project updated = projectRepository.save(project);
        log.info("Updated project ID: {} with slug: {}", id, slug);
        return projectMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROJECTS, allEntries = true)
    public void deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        projectRepository.delete(project);
        log.info("Deleted project with ID: {}", id);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROJECTS, allEntries = true)
    public ProjectResponse setPublishStatus(String id, boolean published) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        project.setPublished(published);
        Project updated = projectRepository.save(project);
        log.info("Updated publish status to {} for project ID: {}", published, id);
        return projectMapper.toResponse(updated);
    }

    private String resolveSlug(String explicitSlug, String title) {
        String raw = StringUtils.hasText(explicitSlug) ? explicitSlug : title;
        if (!StringUtils.hasText(raw)) {
            raw = "project-" + System.currentTimeMillis();
        }

        String nowhitespace = WHITESPACE.matcher(raw.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
        slug = EDGES_DASHES.matcher(slug).replaceAll("");

        if (!StringUtils.hasText(slug)) {
            slug = "project-" + System.currentTimeMillis();
        }
        return slug;
    }
}
