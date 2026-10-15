package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
public class ProfileRequest {

    @NotBlank(message = ValidationMessageConstants.FULL_NAME_REQUIRED)
    @Size(max = 100, message = ValidationMessageConstants.FULL_NAME_MAX_100)
    private String fullName;

    @NotBlank(message = ValidationMessageConstants.PROFESSIONAL_TITLE_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.PROFESSIONAL_TITLE_MAX_150)
    private String professionalTitle;

    @Size(max = 500, message = ValidationMessageConstants.PROFILE_IMAGE_URL_MAX_500)
    private String profileImageUrl;

    @Size(max = 500, message = ValidationMessageConstants.SHORT_INTRO_MAX_500)
    private String shortIntroduction;

    private String aboutMe;

    @NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.EMAIL_INVALID)
    @Size(max = 150, message = ValidationMessageConstants.EMAIL_MAX_150)
    private String email;

    @Size(max = 30, message = ValidationMessageConstants.PHONE_MAX_30)
    private String phoneNumber;

    @Size(max = 150, message = ValidationMessageConstants.LOCATION_MAX_150)
    private String location;

    @Size(max = 500, message = ValidationMessageConstants.RESUME_URL_MAX_500)
    private String resumeUrl;

    @Min(value = 0, message = ValidationMessageConstants.YEARS_OF_EXPERIENCE_MIN_0)
    private Integer yearsOfExperience;

    private boolean availableForWork;
}
