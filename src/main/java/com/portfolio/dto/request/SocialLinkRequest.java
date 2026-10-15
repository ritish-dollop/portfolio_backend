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
public class SocialLinkRequest {

    @NotBlank(message = ValidationMessageConstants.PLATFORM_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.PLATFORM_MAX_100)
    private String platform;

    @NotBlank(message = ValidationMessageConstants.LABEL_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.LABEL_MAX_100)
    private String label;

    @NotBlank(message = ValidationMessageConstants.URL_REQUIRED)
    @Size(max = 500, message = ValidationMessageConstants.URL_MAX_500)
    private String url;

    @Size(max = 100, message = ValidationMessageConstants.ICON_MAX_100)
    private String icon;

    private Integer displayOrder;

    @Builder.Default
    private boolean active = true;
}
