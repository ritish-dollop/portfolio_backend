package com.portfolio.service;

import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.dto.response.ExperienceResponse;
import com.portfolio.entity.Experience;
import com.portfolio.enums.EmploymentType;
import com.portfolio.exception.BadRequestException;
import com.portfolio.mapper.ExperienceMapper;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.service.impl.ExperienceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Spy
    private ExperienceMapper experienceMapper = new ExperienceMapper();

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Test
    @DisplayName("Should create experience with valid dates")
    void createExperience_ValidDates_Success() {
        ExperienceRequest request = ExperienceRequest.builder()
                .companyName("Acme Corp")
                .jobTitle("Backend Engineer")
                .employmentType(EmploymentType.FULL_TIME)
                .startDate(LocalDate.of(2022, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .currentlyWorking(false)
                .responsibilities(List.of("Developed microservices", "Optimized queries"))
                .build();

        when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> {
            Experience saved = invocation.getArgument(0);
            saved.setId("uuid-exp-1");
            return saved;
        });

        ExperienceResponse response = experienceService.createExperience(request);

        assertThat(response).isNotNull();
        assertThat(response.getCompanyName()).isEqualTo("Acme Corp");
        assertThat(response.getJobTitle()).isEqualTo("Backend Engineer");
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when end date is earlier than start date")
    void createExperience_EndDateBeforeStartDate_ThrowsException() {
        ExperienceRequest request = ExperienceRequest.builder()
                .companyName("Acme Corp")
                .jobTitle("Backend Engineer")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2022, 1, 1))
                .currentlyWorking(false)
                .build();

        assertThatThrownBy(() -> experienceService.createExperience(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("end date cannot be earlier than start date");
    }
}
