package com.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private String id;
    private String title;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private String thumbnailUrl;
    private String githubUrl;
    private String liveDemoUrl;
    private boolean featured;
    private boolean published;
    private Integer displayOrder;

    @Builder.Default
    private List<String> technologies = new ArrayList<>();

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
