package com.portfolio.service;

import com.portfolio.dto.request.SiteSettingRequest;
import com.portfolio.dto.response.SiteSettingResponse;

import java.util.List;

public interface SiteSettingService {

    List<SiteSettingResponse> getPublicSettings();

    List<SiteSettingResponse> getAllSettingsForAdmin();

    SiteSettingResponse getSettingByKey(String settingKey);

    SiteSettingResponse createOrUpdateSetting(SiteSettingRequest request);

    void deleteSetting(String id);
}
