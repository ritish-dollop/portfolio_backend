package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.ProfileRequest;
import com.portfolio.dto.response.ProfileResponse;
import com.portfolio.entity.Profile;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProfileMapper;
import com.portfolio.repository.ProfileRepository;
import com.portfolio.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_PROFILE, key = "'public'")
    public ProfileResponse getPublicProfile() {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "record", "primary"));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getAdminProfile() {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "record", "primary"));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROFILE, allEntries = true)
    public ProfileResponse createProfile(ProfileRequest request) {
        if (profileRepository.count() > 0) {
            throw new DuplicateResourceException(
                    "Profile already exists. Only one primary profile record is allowed. Update the existing profile instead.");
        }

        Profile profile = profileMapper.toEntity(request);
        Profile saved = profileRepository.save(profile);
        log.info("Created primary portfolio profile for: {}", saved.getFullName());
        return profileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_PROFILE, allEntries = true)
    public ProfileResponse updateProfile(ProfileRequest request) {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> profileMapper.toEntity(request));

        if (profile.getId() != null) {
            profileMapper.updateEntity(profile, request);
        }

        Profile saved = profileRepository.save(profile);
        log.info("Updated primary portfolio profile for: {}", saved.getFullName());
        return profileMapper.toResponse(saved);
    }
}
