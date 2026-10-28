package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpRequest {

    @NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.EMAIL_INVALID)
    private String email;
}
