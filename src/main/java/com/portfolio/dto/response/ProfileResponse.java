package com.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String id;
    private String fullName;
    private String professionalTitle;
    private String profileImageUrl;
    private String shortIntroduction;
    private String aboutMe;
    private String email;
    private String phoneNumber;
    private String location;
    private String resumeUrl;
    private Integer yearsOfExperience;
    private boolean availableForWork;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
