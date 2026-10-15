package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ExperienceResponse;
import com.portfolio.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_EXPERIENCES)
@RequiredArgsConstructor
public class PublicExperienceController {

    private final ExperienceService experienceService;

    @GetMapping(ApiConstants.GET_ALL_EXPERIENCES)
    public ResponseEntity<ApiResponse<List<ExperienceResponse>>> getAllExperiences() {
        List<ExperienceResponse> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }
}
