package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SkillResponse;
import com.portfolio.enums.SkillCategory;
import com.portfolio.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_SKILLS)
@RequiredArgsConstructor
public class PublicSkillController {

    private final SkillService skillService;

    @GetMapping(ApiConstants.GET_SKILLS)
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getSkills(
            @RequestParam(required = false) SkillCategory category) {
        List<SkillResponse> skills = (category != null)
                ? skillService.getSkillsByCategory(category)
                : skillService.getActiveSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }
}
