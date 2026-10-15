package com.portfolio.service;

import com.portfolio.dto.request.CreateProjectRequest;
import com.portfolio.dto.request.UpdateProjectRequest;
import com.portfolio.dto.response.PagedResponse;
import com.portfolio.dto.response.ProjectResponse;
import com.portfolio.entity.Project;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProjectMapper;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Spy
    private ProjectMapper projectMapper = new ProjectMapper();

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = Project.builder()
                .id("uuid-project-1")
                .title("Portfolio Website")
                .slug("portfolio-website")
                .shortDescription("Dynamic personal portfolio")
                .fullDescription("Complete details about portfolio")
                .published(true)
                .featured(true)
                .displayOrder(1)
                .technologies(List.of("Java", "Spring Boot", "MySQL"))
                .imageUrls(List.of("https://example.com/image1.png"))
                .build();
    }

    @Test
    @DisplayName("Should create project successfully with generated slug")
    void createProject_Success() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .title("E-Commerce Platform")
                .shortDescription("Online store")
                .published(true)
                .build();

        when(projectRepository.existsBySlugIgnoreCase("e-commerce-platform")).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project saved = invocation.getArgument(0);
            saved.setId("uuid-project-2");
            return saved;
        });

        ProjectResponse response = projectService.createProject(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("E-Commerce Platform");
        assertThat(response.getSlug()).isEqualTo("e-commerce-platform");
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when slug already exists on create")
    void createProject_DuplicateSlug_ThrowsException() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .title("Portfolio Website")
                .slug("portfolio-website")
                .build();

        when(projectRepository.existsBySlugIgnoreCase("portfolio-website")).thenReturn(true);

        assertThatThrownBy(() -> projectService.createProject(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("slug");
    }

    @Test
    @DisplayName("Should return published project by slug")
    void getPublishedProjectBySlug_Success() {
        when(projectRepository.findByPublishedTrueAndSlug("portfolio-website"))
                .thenReturn(Optional.of(sampleProject));

        ProjectResponse response = projectService.getPublishedProjectBySlug("portfolio-website");

        assertThat(response).isNotNull();
        assertThat(response.getSlug()).isEqualTo("portfolio-website");
        assertThat(response.isPublished()).isTrue();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when project not found by slug")
    void getPublishedProjectBySlug_NotFound_ThrowsException() {
        when(projectRepository.findByPublishedTrueAndSlug("non-existent"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getPublishedProjectBySlug("non-existent"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should get paginated published projects")
    void getPublishedProjects_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(List.of(sampleProject), pageable, 1);

        when(projectRepository.findAllByPublishedTrue(pageable)).thenReturn(page);

        PagedResponse<ProjectResponse> response = projectService.getPublishedProjects(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update project publish status")
    void setPublishStatus_Success() {
        when(projectRepository.findById("uuid-project-1")).thenReturn(Optional.of(sampleProject));
        when(projectRepository.save(any(Project.class))).thenReturn(sampleProject);

        ProjectResponse response = projectService.setPublishStatus("uuid-project-1", false);

        assertThat(response).isNotNull();
        assertThat(sampleProject.isPublished()).isFalse();
        verify(projectRepository).save(sampleProject);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void deleteProject_Success() {
        when(projectRepository.findById("uuid-project-1")).thenReturn(Optional.of(sampleProject));

        projectService.deleteProject("uuid-project-1");

        verify(projectRepository).delete(sampleProject);
    }
}
