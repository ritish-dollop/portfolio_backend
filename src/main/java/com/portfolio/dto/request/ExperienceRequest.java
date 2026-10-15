package com.portfolio.dto.request;

import com.portfolio.constant.ValidationMessageConstants;
import com.portfolio.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequest {

    @NotBlank(message = ValidationMessageConstants.COMPANY_NAME_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.COMPANY_NAME_MAX_150)
    private String companyName;

    @NotBlank(message = ValidationMessageConstants.JOB_TITLE_REQUIRED)
    @Size(max = 150, message = ValidationMessageConstants.JOB_TITLE_MAX_150)
    private String jobTitle;

    private EmploymentType employmentType;

    @Size(max = 150, message = ValidationMessageConstants.LOCATION_MAX_150)
    private String location;

    @NotNull(message = ValidationMessageConstants.START_DATE_REQUIRED)
    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private boolean currentlyWorking = false;

    @Builder.Default
    private List<String> responsibilities = new ArrayList<>();

    private Integer displayOrder;
}
