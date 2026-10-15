package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingRequest {

    @NotBlank(message = ValidationMessageConstants.SETTING_KEY_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.SETTING_KEY_MAX_100)
    private String settingKey;

    @NotBlank(message = ValidationMessageConstants.SETTING_VALUE_REQUIRED)
    private String settingValue;

    @Size(max = 255, message = ValidationMessageConstants.SETTING_DESCRIPTION_MAX_255)
    private String description;
}
