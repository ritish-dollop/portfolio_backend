package com.portfolio.dto.response;

import com.portfolio.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {

    private String id;
    private String companyName;
    private String jobTitle;
    private EmploymentType employmentType;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean currentlyWorking;

    @Builder.Default
    private List<String> responsibilities = new ArrayList<>();

    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
