package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import com.portfolio.enums.SkillCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSkillRequest {

    @NotBlank(message = ValidationMessageConstants.SKILL_NAME_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.SKILL_NAME_MAX_100)
    private String name;

    @NotNull(message = ValidationMessageConstants.SKILL_CATEGORY_REQUIRED)
    private SkillCategory category;

    @Min(value = 0, message = ValidationMessageConstants.SKILL_PROFICIENCY_MIN_0)
    @Max(value = 100, message = ValidationMessageConstants.SKILL_PROFICIENCY_MAX_100)
    private Integer proficiencyLevel;

    @Size(max = 500, message = ValidationMessageConstants.SKILL_ICON_URL_MAX_500)
    private String iconUrl;

    private Integer displayOrder;

    private boolean active;
}
