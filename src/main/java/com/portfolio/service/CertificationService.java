package com.portfolio.service;

import com.portfolio.dto.request.CertificationRequest;
import com.portfolio.dto.response.CertificationResponse;

import java.util.List;

public interface CertificationService {

    List<CertificationResponse> getAllCertifications();

    CertificationResponse getCertificationById(String id);

    CertificationResponse createCertification(CertificationRequest request);

    CertificationResponse updateCertification(String id, CertificationRequest request);

    void deleteCertification(String id);
}
