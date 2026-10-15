package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.CreateSkillRequest;
import com.portfolio.dto.request.UpdateSkillRequest;
import com.portfolio.dto.response.SkillResponse;
import com.portfolio.entity.Skill;
import com.portfolio.enums.SkillCategory;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.SkillMapper;
import com.portfolio.repository.SkillRepository;
import com.portfolio.service.SkillService;
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
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_SKILLS, key = "'active'")
    public List<SkillResponse> getActiveSkills() {
        return skillRepository.findAllByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_SKILLS, key = "'category_' + #category")
    public List<SkillResponse> getSkillsByCategory(SkillCategory category) {
        return skillRepository.findByCategoryAndActiveTrueOrderByDisplayOrderAsc(category).stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponse> getAllSkillsForAdmin() {
        return skillRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SkillResponse getSkillById(String id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        return skillMapper.toResponse(skill);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SKILLS, allEntries = true)
    public SkillResponse createSkill(CreateSkillRequest request) {
        Skill skill = skillMapper.toEntity(request);
        Skill saved = skillRepository.save(skill);
        log.info("Created new skill: {} (ID: {})", saved.getName(), saved.getId());
        return skillMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SKILLS, allEntries = true)
    public SkillResponse updateSkill(String id, UpdateSkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));

        skillMapper.updateEntity(skill, request);
        Skill updated = skillRepository.save(skill);
        log.info("Updated skill ID: {}", id);
        return skillMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SKILLS, allEntries = true)
    public void deleteSkill(String id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        skillRepository.delete(skill);
        log.info("Deleted skill with ID: {}", id);
    }
}
