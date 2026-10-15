package com.portfolio.mapper;

import com.portfolio.dto.request.ProfileRequest;
import com.portfolio.dto.response.ProfileResponse;
import com.portfolio.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponse toResponse(Profile entity) {
        if (entity == null) {
            return null;
        }
        return ProfileResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .professionalTitle(entity.getProfessionalTitle())
                .profileImageUrl(entity.getProfileImageUrl())
                .shortIntroduction(entity.getShortIntroduction())
                .aboutMe(entity.getAboutMe())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .location(entity.getLocation())
                .resumeUrl(entity.getResumeUrl())
                .yearsOfExperience(entity.getYearsOfExperience())
                .availableForWork(entity.isAvailableForWork())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Profile toEntity(ProfileRequest request) {
        if (request == null) {
            return null;
        }
        return Profile.builder()
                .fullName(request.getFullName())
                .professionalTitle(request.getProfessionalTitle())
                .profileImageUrl(request.getProfileImageUrl())
                .shortIntroduction(request.getShortIntroduction())
                .aboutMe(request.getAboutMe())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .resumeUrl(request.getResumeUrl())
                .yearsOfExperience(request.getYearsOfExperience())
                .availableForWork(request.isAvailableForWork())
                .build();
    }

    public void updateEntity(Profile entity, ProfileRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setFullName(request.getFullName());
        entity.setProfessionalTitle(request.getProfessionalTitle());
        entity.setProfileImageUrl(request.getProfileImageUrl());
        entity.setShortIntroduction(request.getShortIntroduction());
        entity.setAboutMe(request.getAboutMe());
        entity.setEmail(request.getEmail());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setLocation(request.getLocation());
        entity.setResumeUrl(request.getResumeUrl());
        entity.setYearsOfExperience(request.getYearsOfExperience());
        entity.setAvailableForWork(request.isAvailableForWork());
    }
}
