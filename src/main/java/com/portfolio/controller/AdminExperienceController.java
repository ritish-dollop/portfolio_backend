package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ExperienceResponse;
import com.portfolio.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADMIN_EXPERIENCES)
@RequiredArgsConstructor
public class AdminExperienceController {

    private final ExperienceService experienceService;

    @GetMapping(ApiConstants.GET_ALL_EXPERIENCES)
    public ResponseEntity<ApiResponse<List<ExperienceResponse>>> getAllExperiences() {
        List<ExperienceResponse> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }

    @GetMapping(ApiConstants.GET_EXPERIENCE_BY_ID)
    public ResponseEntity<ApiResponse<ExperienceResponse>> getExperienceById(@PathVariable String id) {
        ExperienceResponse experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(ApiResponse.success(experience));
    }

    @PostMapping(ApiConstants.CREATE_EXPERIENCE)
    public ResponseEntity<ApiResponse<ExperienceResponse>> createExperience(
            @Valid @RequestBody ExperienceRequest request) {
        ExperienceResponse created = experienceService.createExperience(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_EXPERIENCE_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_EXPERIENCE)
    public ResponseEntity<ApiResponse<ExperienceResponse>> updateExperience(
            @PathVariable String id,
            @Valid @RequestBody ExperienceRequest request) {
        ExperienceResponse updated = experienceService.updateExperience(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_EXPERIENCE_UPDATED_SUCCESS, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_EXPERIENCE)
    public ResponseEntity<Void> deleteExperience(@PathVariable String id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
