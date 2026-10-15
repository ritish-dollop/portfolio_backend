package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.ProfileRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.ProfileResponse;
import com.portfolio.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ADMIN_PROFILE)
@RequiredArgsConstructor
public class AdminProfileController {

    private final ProfileService profileService;

    @GetMapping(ApiConstants.GET_ADMIN_PROFILE)
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileForAdmin() {
        ProfileResponse response = profileService.getAdminProfile();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping(ApiConstants.CREATE_PROFILE)
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(AppConstants.MSG_PROFILE_CREATED_SUCCESS, response));
    }

    @PutMapping(ApiConstants.UPDATE_PROFILE)
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_PROFILE_UPDATED_SUCCESS, response));
    }
}
