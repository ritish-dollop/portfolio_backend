package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.dto.response.ExperienceResponse;
import com.portfolio.entity.Experience;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ExperienceMapper;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.service.ExperienceService;
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
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_EXPERIENCES, key = "'all'")
    public List<ExperienceResponse> getAllExperiences() {
        return experienceRepository.findAllByOrderByStartDateDesc().stream()
                .map(experienceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceResponse getExperienceById(String id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));
        return experienceMapper.toResponse(experience);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EXPERIENCES, allEntries = true)
    public ExperienceResponse createExperience(ExperienceRequest request) {
        validateDateRange(request);

        Experience experience = experienceMapper.toEntity(request);
        Experience saved = experienceRepository.save(experience);
        log.info("Created experience record: {} at {} (ID: {})", saved.getJobTitle(), saved.getCompanyName(), saved.getId());
        return experienceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EXPERIENCES, allEntries = true)
    public ExperienceResponse updateExperience(String id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        validateDateRange(request);

        experienceMapper.updateEntity(experience, request);
        Experience updated = experienceRepository.save(experience);
        log.info("Updated experience record ID: {}", id);
        return experienceMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_EXPERIENCES, allEntries = true)
    public void deleteExperience(String id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));
        experienceRepository.delete(experience);
        log.info("Deleted experience record with ID: {}", id);
    }

    private void validateDateRange(ExperienceRequest request) {
        if (!request.isCurrentlyWorking() && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new BadRequestException("Experience end date cannot be earlier than start date");
            }
        }
    }
}
