package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.SiteSettingRequest;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SiteSettingResponse;
import com.portfolio.service.SiteSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADMIN_SITE_SETTINGS)
@RequiredArgsConstructor
public class AdminSiteSettingController {

    private final SiteSettingService siteSettingService;

    @GetMapping(ApiConstants.GET_ALL_SETTINGS)
    public ResponseEntity<ApiResponse<List<SiteSettingResponse>>> getAllSettings() {
        List<SiteSettingResponse> settings = siteSettingService.getAllSettingsForAdmin();
        return ResponseEntity.ok(ApiResponse.success(settings));
    }

    @GetMapping(ApiConstants.GET_SETTING_BY_KEY)
    public ResponseEntity<ApiResponse<SiteSettingResponse>> getSettingByKey(@PathVariable String key) {
        SiteSettingResponse setting = siteSettingService.getSettingByKey(key);
        return ResponseEntity.ok(ApiResponse.success(setting));
    }

    @PostMapping(ApiConstants.CREATE_OR_UPDATE_SETTING)
    public ResponseEntity<ApiResponse<SiteSettingResponse>> createOrUpdateSetting(
            @Valid @RequestBody SiteSettingRequest request) {
        SiteSettingResponse saved = siteSettingService.createOrUpdateSetting(request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_SITE_SETTING_SAVED_SUCCESS, saved));
    }

    @DeleteMapping(ApiConstants.DELETE_SETTING)
    public ResponseEntity<Void> deleteSetting(@PathVariable String id) {
        siteSettingService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
