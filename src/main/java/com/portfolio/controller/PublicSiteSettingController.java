package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.SiteSettingResponse;
import com.portfolio.service.SiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.PUBLIC_SITE_SETTINGS)
@RequiredArgsConstructor
public class PublicSiteSettingController {

    private final SiteSettingService siteSettingService;

    @GetMapping(ApiConstants.GET_PUBLIC_SETTINGS)
    public ResponseEntity<ApiResponse<List<SiteSettingResponse>>> getPublicSettings() {
        List<SiteSettingResponse> settings = siteSettingService.getPublicSettings();
        return ResponseEntity.ok(ApiResponse.success(settings));
    }

    @GetMapping(ApiConstants.GET_SETTING_BY_KEY)
    public ResponseEntity<ApiResponse<SiteSettingResponse>> getSettingByKey(@PathVariable String key) {
        SiteSettingResponse setting = siteSettingService.getSettingByKey(key);
        return ResponseEntity.ok(ApiResponse.success(setting));
    }
}
