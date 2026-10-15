package com.portfolio.service;

import com.portfolio.dto.request.ProfileRequest;
import com.portfolio.dto.response.ProfileResponse;
import com.portfolio.entity.Profile;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProfileMapper;
import com.portfolio.repository.ProfileRepository;
import com.portfolio.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Spy
    private ProfileMapper profileMapper = new ProfileMapper();

    @InjectMocks
    private ProfileServiceImpl profileService;

    private Profile sampleProfile;

    @BeforeEach
    void setUp() {
        sampleProfile = Profile.builder()
                .id("uuid-profile-1")
                .fullName("John Doe")
                .professionalTitle("Senior Full Stack Developer")
                .email("john@example.com")
                .location("New York, USA")
                .yearsOfExperience(7)
                .availableForWork(true)
                .build();
    }

    @Test
    @DisplayName("Should return public profile successfully")
    void getPublicProfile_Success() {
        when(profileRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(sampleProfile));

        ProfileResponse response = profileService.getPublicProfile();

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("John Doe");
        assertThat(response.getProfessionalTitle()).isEqualTo("Senior Full Stack Developer");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when profile not found")
    void getPublicProfile_NotFound_ThrowsException() {
        when(profileRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getPublicProfile())
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should prevent creating multiple profiles")
    void createProfile_Duplicate_ThrowsException() {
        ProfileRequest request = ProfileRequest.builder()
                .fullName("Jane Doe")
                .professionalTitle("Backend Developer")
                .email("jane@example.com")
                .build();

        when(profileRepository.count()).thenReturn(1L);

        assertThatThrownBy(() -> profileService.createProfile(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Profile already exists");

        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update existing profile successfully")
    void updateProfile_Success() {
        ProfileRequest request = ProfileRequest.builder()
                .fullName("John Updated")
                .professionalTitle("Lead Architect")
                .email("john.updated@example.com")
                .availableForWork(false)
                .build();

        when(profileRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(sampleProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(sampleProfile);

        ProfileResponse response = profileService.updateProfile(request);

        assertThat(response).isNotNull();
        assertThat(sampleProfile.getFullName()).isEqualTo("John Updated");
        assertThat(sampleProfile.getProfessionalTitle()).isEqualTo("Lead Architect");
        verify(profileRepository).save(sampleProfile);
    }
}
