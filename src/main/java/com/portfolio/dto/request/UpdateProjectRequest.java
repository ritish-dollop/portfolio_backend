package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    @NotBlank(message = ValidationMessageConstants.PROJECT_TITLE_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.PROJECT_TITLE_MAX_150)
    private String title;

    @Size(max = 150, message = ValidationMessageConstants.PROJECT_SLUG_MAX_150)
    private String slug;

    @Size(max = 500, message = ValidationMessageConstants.PROJECT_SHORT_DESC_MAX_500)
    private String shortDescription;

    private String fullDescription;

    @Size(max = 500, message = ValidationMessageConstants.PROJECT_THUMBNAIL_URL_MAX_500)
    private String thumbnailUrl;

    @Size(max = 500, message = ValidationMessageConstants.PROJECT_GITHUB_URL_MAX_500)
    private String githubUrl;

    @Size(max = 500, message = ValidationMessageConstants.PROJECT_LIVE_DEMO_URL_MAX_500)
    private String liveDemoUrl;

    private boolean featured;

    private boolean published;

    private Integer displayOrder;

    @Builder.Default
    private List<String> technologies = new ArrayList<>();

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();
}
