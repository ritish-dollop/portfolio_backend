package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.EducationResponse;
import com.portfolio.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_EDUCATIONS)
@RequiredArgsConstructor
public class PublicEducationController {

    private final EducationService educationService;

    @GetMapping(ApiConstants.GET_ALL_EDUCATIONS)
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllEducations() {
        List<EducationResponse> educations = educationService.getAllEducations();
        return ResponseEntity.ok(ApiResponse.success(educations));
    }
}
