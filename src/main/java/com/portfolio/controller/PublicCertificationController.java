package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.CertificationResponse;
import com.portfolio.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_CERTIFICATIONS)
@RequiredArgsConstructor
public class PublicCertificationController {

    private final CertificationService certificationService;

    @GetMapping(ApiConstants.GET_ALL_CERTIFICATIONS)
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getAllCertifications() {
        List<CertificationResponse> certifications = certificationService.getAllCertifications();
        return ResponseEntity.ok(ApiResponse.success(certifications));
    }
}
