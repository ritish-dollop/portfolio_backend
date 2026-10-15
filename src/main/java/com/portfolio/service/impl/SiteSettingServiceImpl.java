package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.SiteSettingRequest;
import com.portfolio.dto.response.SiteSettingResponse;
import com.portfolio.entity.SiteSetting;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.SiteSettingMapper;
import com.portfolio.repository.SiteSettingRepository;
import com.portfolio.service.SiteSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteSettingServiceImpl implements SiteSettingService {

    private static final Set<String> PROHIBITED_KEY_PATTERNS = Set.of(
            "password", "secret", "jwt", "token", "credential", "private_key", "api_key"
    );

    private final SiteSettingRepository siteSettingRepository;
    private final SiteSettingMapper siteSettingMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_SITE_SETTINGS, key = "'public'")
    public List<SiteSettingResponse> getPublicSettings() {
        return siteSettingRepository.findAll().stream()
                .filter(setting -> !isSensitiveKey(setting.getSettingKey()))
                .map(siteSettingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteSettingResponse> getAllSettingsForAdmin() {
        return siteSettingRepository.findAll().stream()
                .map(siteSettingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_SITE_SETTINGS, key = "'key_' + #settingKey")
    public SiteSettingResponse getSettingByKey(String settingKey) {
        SiteSetting setting = siteSettingRepository.findBySettingKey(settingKey.trim())
                .orElseThrow(() -> new ResourceNotFoundException("SiteSetting", "key", settingKey));
        return siteSettingMapper.toResponse(setting);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SITE_SETTINGS, allEntries = true)
    public SiteSettingResponse createOrUpdateSetting(SiteSettingRequest request) {
        String key = request.getSettingKey().trim();

        if (isSensitiveKey(key)) {
            throw new BadRequestException("Storing sensitive credentials, keys, or secrets in site settings is strictly prohibited");
        }

        SiteSetting setting = siteSettingRepository.findBySettingKey(key)
                .orElseGet(() -> siteSettingMapper.toEntity(request));

        if (setting.getId() != null) {
            siteSettingMapper.updateEntity(setting, request);
        }

        SiteSetting saved = siteSettingRepository.save(setting);
        log.info("Saved site setting with key: {}", saved.getSettingKey());
        return siteSettingMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SITE_SETTINGS, allEntries = true)
    public void deleteSetting(String id) {
        SiteSetting setting = siteSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SiteSetting", "id", id));
        siteSettingRepository.delete(setting);
        log.info("Deleted site setting with ID: {}", id);
    }

    private boolean isSensitiveKey(String key) {
        if (key == null) {
            return false;
        }
        String lowerKey = key.toLowerCase(Locale.ROOT);
        return PROHIBITED_KEY_PATTERNS.stream().anyMatch(lowerKey::contains);
    }
}
