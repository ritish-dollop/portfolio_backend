package com.portfolio.mapper;

import com.portfolio.dto.request.CertificationRequest;
import com.portfolio.dto.response.CertificationResponse;
import com.portfolio.entity.Certification;
import org.springframework.stereotype.Component;

@Component
public class CertificationMapper {

    public CertificationResponse toResponse(Certification entity) {
        if (entity == null) {
            return null;
        }
        return CertificationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .issuingOrganization(entity.getIssuingOrganization())
                .issueDate(entity.getIssueDate())
                .expirationDate(entity.getExpirationDate())
                .credentialId(entity.getCredentialId())
                .credentialUrl(entity.getCredentialUrl())
                .certificateImageUrl(entity.getCertificateImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Certification toEntity(CertificationRequest request) {
        if (request == null) {
            return null;
        }
        return Certification.builder()
                .name(request.getName())
                .issuingOrganization(request.getIssuingOrganization())
                .issueDate(request.getIssueDate())
                .expirationDate(request.getExpirationDate())
                .credentialId(request.getCredentialId())
                .credentialUrl(request.getCredentialUrl())
                .certificateImageUrl(request.getCertificateImageUrl())
                .build();
    }

    public void updateEntity(Certification entity, CertificationRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
        entity.setIssuingOrganization(request.getIssuingOrganization());
        entity.setIssueDate(request.getIssueDate());
        entity.setExpirationDate(request.getExpirationDate());
        entity.setCredentialId(request.getCredentialId());
        entity.setCredentialUrl(request.getCredentialUrl());
        entity.setCertificateImageUrl(request.getCertificateImageUrl());
    }
}
