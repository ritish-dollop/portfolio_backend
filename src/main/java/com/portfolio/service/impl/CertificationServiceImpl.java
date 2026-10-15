package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.CertificationRequest;
import com.portfolio.dto.response.CertificationResponse;
import com.portfolio.entity.Certification;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.CertificationMapper;
import com.portfolio.repository.CertificationRepository;
import com.portfolio.service.CertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final CertificationMapper certificationMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_CERTIFICATIONS, key = "'all'")
    public List<CertificationResponse> getAllCertifications() {
        return certificationRepository.findAllByOrderByIssueDateDesc().stream()
                .map(certificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CertificationResponse getCertificationById(String id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", id));
        return certificationMapper.toResponse(certification);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_CERTIFICATIONS, allEntries = true)
    public CertificationResponse createCertification(CertificationRequest request) {
        validateDateRange(request);

        Certification certification = certificationMapper.toEntity(request);
        Certification saved = certificationRepository.save(certification);
        log.info("Created certification: {} by {} (ID: {})", saved.getName(), saved.getIssuingOrganization(), saved.getId());
        return certificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_CERTIFICATIONS, allEntries = true)
    public CertificationResponse updateCertification(String id, CertificationRequest request) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", id));

        validateDateRange(request);

        certificationMapper.updateEntity(certification, request);
        Certification updated = certificationRepository.save(certification);
        log.info("Updated certification ID: {}", id);
        return certificationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_CERTIFICATIONS, allEntries = true)
    public void deleteCertification(String id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", id));
        certificationRepository.delete(certification);
        log.info("Deleted certification with ID: {}", id);
    }

    private void validateDateRange(CertificationRequest request) {
        if (request.getExpirationDate() != null && request.getIssueDate() != null) {
            if (request.getExpirationDate().isBefore(request.getIssueDate())) {
                throw new BadRequestException("Certification expiration date cannot be earlier than issue date");
            }
        }
    }
}
