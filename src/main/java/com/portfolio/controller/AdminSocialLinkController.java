package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.SocialLinkRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SocialLinkResponse;
import com.portfolio.service.SocialLinkService;
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
@RequestMapping(ApiConstants.ADMIN_SOCIAL_LINKS)
@RequiredArgsConstructor
public class AdminSocialLinkController {

    private final SocialLinkService socialLinkService;

    @GetMapping(ApiConstants.GET_ALL_SOCIAL_LINKS)
    public ResponseEntity<ApiResponse<List<SocialLinkResponse>>> getAllSocialLinks() {
        List<SocialLinkResponse> links = socialLinkService.getAllSocialLinksForAdmin();
        return ResponseEntity.ok(ApiResponse.success(links));
    }

    @GetMapping(ApiConstants.GET_SOCIAL_LINK_BY_ID)
    public ResponseEntity<ApiResponse<SocialLinkResponse>> getSocialLinkById(@PathVariable String id) {
        SocialLinkResponse link = socialLinkService.getSocialLinkById(id);
        return ResponseEntity.ok(ApiResponse.success(link));
    }

    @PostMapping(ApiConstants.CREATE_SOCIAL_LINK)
    public ResponseEntity<ApiResponse<SocialLinkResponse>> createSocialLink(
            @Valid @RequestBody SocialLinkRequest request) {
        SocialLinkResponse created = socialLinkService.createSocialLink(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_SOCIAL_LINK_CREATED_SUCCESS, created));
    }

    @PutMapping(ApiConstants.UPDATE_SOCIAL_LINK)
    public ResponseEntity<ApiResponse<SocialLinkResponse>> updateSocialLink(
            @PathVariable String id,
            @Valid @RequestBody SocialLinkRequest request) {
        SocialLinkResponse updated = socialLinkService.updateSocialLink(id, request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_SOCIAL_LINK_UPDATED_SUCCESS, updated));
    }

    @DeleteMapping(ApiConstants.DELETE_SOCIAL_LINK)
    public ResponseEntity<Void> deleteSocialLink(@PathVariable String id) {
        socialLinkService.deleteSocialLink(id);
        return ResponseEntity.noContent().build();
    }
}
