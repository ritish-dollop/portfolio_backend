package com.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingResponse {

    private String id;
    private String settingKey;
    private String settingValue;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
