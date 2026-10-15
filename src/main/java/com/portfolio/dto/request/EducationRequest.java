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
public class EducationRequest {

    @NotBlank(message = ValidationMessageConstants.DEGREE_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.DEGREE_MAX_150)
    private String degree;

    @NotBlank(message = ValidationMessageConstants.INSTITUTION_REQUIRED)
    @Size(max = 200, message = ValidationMessageConstants.INSTITUTION_MAX_200)
    private String institution;

    @Size(max = 150, message = ValidationMessageConstants.FIELD_OF_STUDY_MAX_150)
    private String fieldOfStudy;

    @NotNull(message = ValidationMessageConstants.START_DATE_REQUIRED)
    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 50, message = ValidationMessageConstants.GRADE_MAX_50)
    private String grade;

    private String description;
}
