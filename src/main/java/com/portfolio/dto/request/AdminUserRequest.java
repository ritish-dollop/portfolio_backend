package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import com.portfolio.enums.Role;
import jakarta.validation.constraints.Email;
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
public class AdminUserRequest {

    @NotBlank(message = ValidationMessageConstants.FULL_NAME_REQUIRED)
    @Size(max = 50, message = ValidationMessageConstants.FULL_NAME_MAX_100)
    private String fullName;

    @NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.EMAIL_INVALID)
    @Size(max = 150, message = ValidationMessageConstants.EMAIL_MAX_150)
    private String email;

    @NotBlank(message = ValidationMessageConstants.PASSWORD_REQUIRED)
    @Size(min = 6, message = ValidationMessageConstants.PASSWORD_MIN_6)
    private String password;

    private Role role;

    @Builder.Default
    private boolean enabled = true;
}
