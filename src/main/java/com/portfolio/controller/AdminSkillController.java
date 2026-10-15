package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.CreateSkillRequest;
import com.portfolio.dto.request.UpdateSkillRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SkillResponse;
import com.portfolio.service.SkillService;
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
@RequestMapping(ApiConstants.ADMIN_SKILLS)
@RequiredArgsConstructor
public class AdminSkillController {

    private final SkillService skillService;

    @GetMapping(ApiConstants.GET_ALL_SKILLS)
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAllSkills() {
        List<SkillResponse> skills = skillService.getAllSkillsForAdmin();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping(ApiConstants.GET_SKILL_BY_ID)
    public ResponseEntity<ApiResponse<SkillResponse>> getSkillById(@PathVariable String id) {
        SkillResponse skill = skillService.getSkillById(id);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    @PostMapping(ApiConstants.CREATE_SKILL)
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(@Valid @RequestBody CreateSkillRequest request) {
        SkillResponse created = skillService.createSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_SKILL_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_SKILL)
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(
            @PathVariable String id,
            @Valid @RequestBody UpdateSkillRequest request) {
        SkillResponse updated = skillService.updateSkill(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_SKILL_UPDATED_SUCCESS, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_SKILL)
    public ResponseEntity<Void> deleteSkill(@PathVariable String id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
