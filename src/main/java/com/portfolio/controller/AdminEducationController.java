package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.EducationRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.EducationResponse;
import com.portfolio.service.EducationService;
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
@RequestMapping(ApiConstants.ADMIN_EDUCATIONS)
@RequiredArgsConstructor
public class AdminEducationController {

    private final EducationService educationService;

    @GetMapping(ApiConstants.GET_ALL_EDUCATIONS)
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllEducations() {
        List<EducationResponse> educations = educationService.getAllEducations();
        return ResponseEntity.ok(ApiResponse.success(educations));
    }

    @GetMapping(ApiConstants.GET_EDUCATION_BY_ID)
    public ResponseEntity<ApiResponse<EducationResponse>> getEducationById(@PathVariable String id) {
        EducationResponse education = educationService.getEducationById(id);
        return ResponseEntity.ok(ApiResponse.success(education));
    }

    @PostMapping(ApiConstants.CREATE_EDUCATION)
    public ResponseEntity<ApiResponse<EducationResponse>> createEducation(
            @Valid @RequestBody EducationRequest request) {
        EducationResponse created = educationService.createEducation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_EDUCATION_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_EDUCATION)
    public ResponseEntity<ApiResponse<EducationResponse>> updateEducation(
            @PathVariable String id,
            @Valid @RequestBody EducationRequest request) {
        EducationResponse updated = educationService.updateEducation(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_EDUCATION_UPDATED_SUCCESS, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_EDUCATION)
    public ResponseEntity<Void> deleteEducation(@PathVariable String id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}
