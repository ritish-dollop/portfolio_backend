package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.EducationRequest;
import com.portfolio.dto.response.EducationResponse;
import com.portfolio.entity.Education;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.EducationMapper;
import com.portfolio.repository.EducationRepository;
import com.portfolio.service.EducationService;
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
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_EDUCATIONS, key = "'all'")
    public List<EducationResponse> getAllEducations() {
        return educationRepository.findAllByOrderByStartDateDesc().stream()
                .map(educationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EducationResponse getEducationById(String id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", id));
        return educationMapper.toResponse(education);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EDUCATIONS, allEntries = true)
    public EducationResponse createEducation(EducationRequest request) {
        validateDateRange(request);

        Education education = educationMapper.toEntity(request);
        Education saved = educationRepository.save(education);
        log.info("Created education record: {} at {} (ID: {})", saved.getDegree(), saved.getInstitution(), saved.getId());
        return educationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EDUCATIONS, allEntries = true)
    public EducationResponse updateEducation(String id, EducationRequest request) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", id));

        validateDateRange(request);

        educationMapper.updateEntity(education, request);
        Education updated = educationRepository.save(education);
        log.info("Updated education record ID: {}", id);
        return educationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EDUCATIONS, allEntries = true)
    public void deleteEducation(String id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", id));
        educationRepository.delete(education);
        log.info("Deleted education record with ID: {}", id);
    }

    private void validateDateRange(EducationRequest request) {
        if (request.getEndDate() != null && request.getStartDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new BadRequestException("Education end date cannot be earlier than start date");
            }
        }
    }
}
