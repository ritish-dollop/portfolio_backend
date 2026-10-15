package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SocialLinkResponse;
import com.portfolio.service.SocialLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_SOCIAL_LINKS)
@RequiredArgsConstructor
public class PublicSocialLinkController {

    private final SocialLinkService socialLinkService;

    @GetMapping(ApiConstants.GET_ACTIVE_SOCIAL_LINKS)
    public ResponseEntity<ApiResponse<List<SocialLinkResponse>>> getActiveSocialLinks() {
        List<SocialLinkResponse> socialLinks = socialLinkService.getActiveSocialLinks();
        return ResponseEntity.ok(ApiResponse.success(socialLinks));
    }
}
