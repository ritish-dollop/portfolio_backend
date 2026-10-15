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
public class AdminPasswordUpdateRequest {

    @NotBlank(message = ValidationMessageConstants.CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;

    @NotBlank(message = ValidationMessageConstants.NEW_PASSWORD_REQUIRED)
    @Size(min = 6, message = ValidationMessageConstants.NEW_PASSWORD_MIN_6)
    private String newPassword;
}
