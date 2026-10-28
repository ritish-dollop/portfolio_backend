package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ProfileResponse;
import com.portfolio.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.PUBLIC_PROFILE)
@RequiredArgsConstructor
public class PublicProfileController {

    private final ProfileService profileService;

    @GetMapping({"", ApiConstants.GET_PUBLIC_PROFILE})
    public ResponseEntity<ApiResponse<ProfileResponse>> getPublicProfile() {
        ProfileResponse response = profileService.getPublicProfile();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
