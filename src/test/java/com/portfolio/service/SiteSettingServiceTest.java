package com.portfolio.service;

import com.portfolio.dto.request.SiteSettingRequest;
import com.portfolio.dto.response.SiteSettingResponse;
import com.portfolio.entity.SiteSetting;
import com.portfolio.exception.BadRequestException;
import com.portfolio.mapper.SiteSettingMapper;
import com.portfolio.repository.SiteSettingRepository;
import com.portfolio.service.impl.SiteSettingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteSettingServiceTest {

    @Mock
    private SiteSettingRepository siteSettingRepository;

    @Spy
    private SiteSettingMapper siteSettingMapper = new SiteSettingMapper();

    @InjectMocks
    private SiteSettingServiceImpl siteSettingService;

    @Test
    @DisplayName("Should save normal site setting successfully")
    void createOrUpdateSetting_Success() {
        SiteSettingRequest request = SiteSettingRequest.builder()
                .settingKey("site_theme")
                .settingValue("dark")
                .description("Default theme for portfolio")
                .build();

        when(siteSettingRepository.findBySettingKey("site_theme")).thenReturn(Optional.empty());
        when(siteSettingRepository.save(any(SiteSetting.class))).thenAnswer(invocation -> {
            SiteSetting setting = invocation.getArgument(0);
            setting.setId("uuid-setting-1");
            return setting;
        });

        SiteSettingResponse response = siteSettingService.createOrUpdateSetting(request);

        assertThat(response).isNotNull();
        assertThat(response.getSettingKey()).isEqualTo("site_theme");
        assertThat(response.getSettingValue()).isEqualTo("dark");
        verify(siteSettingRepository).save(any(SiteSetting.class));
    }

    @Test
    @DisplayName("Should reject storing sensitive credentials in site settings")
    void createOrUpdateSetting_SensitiveKey_ThrowsException() {
        SiteSettingRequest request = SiteSettingRequest.builder()
                .settingKey("admin_secret_key")
                .settingValue("my-secret-123")
                .build();

        assertThatThrownBy(() -> siteSettingService.createOrUpdateSetting(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("sensitive credentials");

        verify(siteSettingRepository, never()).save(any());
    }
}
