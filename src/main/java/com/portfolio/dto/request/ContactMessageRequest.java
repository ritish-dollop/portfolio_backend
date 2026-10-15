package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
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
public class ContactMessageRequest {

    @NotBlank(message = ValidationMessageConstants.CONTACT_NAME_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.CONTACT_NAME_MAX_100)
    private String name;

    @NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.CONTACT_EMAIL_INVALID)
    @Size(max = 150, message = ValidationMessageConstants.EMAIL_MAX_150)
    private String email;

    @NotBlank(message = ValidationMessageConstants.CONTACT_SUBJECT_REQUIRED)
    @Size(max = 200, message = ValidationMessageConstants.CONTACT_SUBJECT_MAX_200)
    private String subject;

    @NotBlank(message = ValidationMessageConstants.CONTACT_MESSAGE_REQUIRED)
    @Size(max = 5000, message = ValidationMessageConstants.CONTACT_MESSAGE_MAX_5000)
    private String message;
}
