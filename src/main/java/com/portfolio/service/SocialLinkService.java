package com.portfolio.service;

import com.portfolio.dto.request.SocialLinkRequest;
import com.portfolio.dto.response.SocialLinkResponse;

import java.util.List;

public interface SocialLinkService {

    List<SocialLinkResponse> getActiveSocialLinks();

    List<SocialLinkResponse> getAllSocialLinksForAdmin();

    SocialLinkResponse getSocialLinkById(String id);

    SocialLinkResponse createSocialLink(SocialLinkRequest request);

    SocialLinkResponse updateSocialLink(String id, SocialLinkRequest request);

    void deleteSocialLink(String id);
}
