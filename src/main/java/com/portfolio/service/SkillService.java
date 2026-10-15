package com.portfolio.service;

import com.portfolio.dto.request.CreateSkillRequest;
import com.portfolio.dto.request.UpdateSkillRequest;
import com.portfolio.dto.response.SkillResponse;
import com.portfolio.enums.SkillCategory;

import java.util.List;

public interface SkillService {

    List<SkillResponse> getActiveSkills();

    List<SkillResponse> getSkillsByCategory(SkillCategory category);

    List<SkillResponse> getAllSkillsForAdmin();

    SkillResponse getSkillById(String id);

    SkillResponse createSkill(CreateSkillRequest request);

    SkillResponse updateSkill(String id, UpdateSkillRequest request);

    void deleteSkill(String id);
}
