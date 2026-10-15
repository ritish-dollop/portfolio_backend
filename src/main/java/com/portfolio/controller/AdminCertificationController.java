package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.CertificationRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.CertificationResponse;
import com.portfolio.service.CertificationService;
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
@RequestMapping(ApiConstants.ADMIN_CERTIFICATIONS)
@RequiredArgsConstructor
public class AdminCertificationController {

    private final CertificationService certificationService;

    @GetMapping(ApiConstants.GET_ALL_CERTIFICATIONS)
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getAllCertifications() {
        List<CertificationResponse> certifications = certificationService.getAllCertifications();
        return ResponseEntity.ok(ApiResponse.success(certifications));
    }

    @GetMapping(ApiConstants.GET_CERTIFICATION_BY_ID)
    public ResponseEntity<ApiResponse<CertificationResponse>> getCertificationById(@PathVariable String id) {
        CertificationResponse certification = certificationService.getCertificationById(id);
        return ResponseEntity.ok(ApiResponse.success(certification));
    }

    @PostMapping(ApiConstants.CREATE_CERTIFICATION)
    public ResponseEntity<ApiResponse<CertificationResponse>> createCertification(
            @Valid @RequestBody CertificationRequest request) {
        CertificationResponse created = certificationService.createCertification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_CERTIFICATION_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_CERTIFICATION)
    public ResponseEntity<ApiResponse<CertificationResponse>> updateCertification(
            @PathVariable String id,
            @Valid @RequestBody CertificationRequest request) {
        CertificationResponse updated = certificationService.updateCertification(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_CERTIFICATION_UPDATED_SUCCESS, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_CERTIFICATION)
    public ResponseEntity<Void> deleteCertification(@PathVariable String id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
