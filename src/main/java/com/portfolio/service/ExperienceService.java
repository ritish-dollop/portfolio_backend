package com.portfolio.service;

import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.dto.response.ExperienceResponse;

import java.util.List;

public interface ExperienceService {

    List<ExperienceResponse> getAllExperiences();

    ExperienceResponse getExperienceById(String id);

    ExperienceResponse createExperience(ExperienceRequest request);

    ExperienceResponse updateExperience(String id, ExperienceRequest request);

    void deleteExperience(String id);
}
