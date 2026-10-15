package com.portfolio.service.impl;

import com.portfolio.constant.CacheConstants;
import com.portfolio.dto.request.SocialLinkRequest;
import com.portfolio.dto.response.SocialLinkResponse;
import com.portfolio.entity.SocialLink;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.SocialLinkMapper;
import com.portfolio.repository.SocialLinkRepository;
import com.portfolio.service.SocialLinkService;
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
public class SocialLinkServiceImpl implements SocialLinkService {

    private final SocialLinkRepository socialLinkRepository;
    private final SocialLinkMapper socialLinkMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.CACHE_SOCIAL_LINKS, key = "'active'")
    public List<SocialLinkResponse> getActiveSocialLinks() {
        return socialLinkRepository.findAllByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(socialLinkMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocialLinkResponse> getAllSocialLinksForAdmin() {
        return socialLinkRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(socialLinkMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SocialLinkResponse getSocialLinkById(String id) {
        SocialLink socialLink = socialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", id));
        return socialLinkMapper.toResponse(socialLink);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SOCIAL_LINKS, allEntries = true)
    public SocialLinkResponse createSocialLink(SocialLinkRequest request) {
        SocialLink socialLink = socialLinkMapper.toEntity(request);
        SocialLink saved = socialLinkRepository.save(socialLink);
        log.info("Created social link: {} (ID: {})", saved.getLabel(), saved.getId());
        return socialLinkMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SOCIAL_LINKS, allEntries = true)
    public SocialLinkResponse updateSocialLink(String id, SocialLinkRequest request) {
        SocialLink socialLink = socialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", id));

        socialLinkMapper.updateEntity(socialLink, request);
        SocialLink updated = socialLinkRepository.save(socialLink);
        log.info("Updated social link ID: {}", id);
        return socialLinkMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConstants.CACHE_SOCIAL_LINKS, allEntries = true)
    public void deleteSocialLink(String id) {
        SocialLink socialLink = socialLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", id));
        socialLinkRepository.delete(socialLink);
        log.info("Deleted social link with ID: {}", id);
    }
}
