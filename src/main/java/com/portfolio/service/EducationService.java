package com.portfolio.service;

import com.portfolio.dto.request.EducationRequest;
import com.portfolio.dto.response.EducationResponse;

import java.util.List;

public interface EducationService {

    List<EducationResponse> getAllEducations();

    EducationResponse getEducationById(String id);

    EducationResponse createEducation(EducationRequest request);

    EducationResponse updateEducation(String id, EducationRequest request);

    void deleteEducation(String id);
}
