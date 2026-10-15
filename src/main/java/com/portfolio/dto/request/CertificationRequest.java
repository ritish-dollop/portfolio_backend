package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationRequest {

    @NotBlank(message = ValidationMessageConstants.CERTIFICATION_NAME_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.CERTIFICATION_NAME_MAX_150)
    private String name;

    @NotBlank(message = ValidationMessageConstants.ISSUING_ORGANIZATION_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.ISSUING_ORGANIZATION_MAX_150)
    private String issuingOrganization;

    @NotNull(message = ValidationMessageConstants.ISSUE_DATE_REQUIRED)
    private LocalDate issueDate;

    private LocalDate expirationDate;

    @Size(max = 150, message = ValidationMessageConstants.CREDENTIAL_ID_MAX_150)
    private String credentialId;

    @Size(max = 500, message = ValidationMessageConstants.CREDENTIAL_URL_MAX_500)
    private String credentialUrl;

    @Size(max = 500, message = ValidationMessageConstants.CERTIFICATE_IMAGE_URL_MAX_500)
    private String certificateImageUrl;
}
