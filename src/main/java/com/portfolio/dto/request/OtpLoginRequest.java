package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpLoginRequest {

    @NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationMessageConstants.OTP_REQUIRED)
    @Pattern(regexp = "^\\d{6}$", message = ValidationMessageConstants.OTP_INVALID_FORMAT)
    private String otp;
}
