package com.portfolio.service;

import com.portfolio.dto.request.AdminPasswordUpdateRequest;
import com.portfolio.dto.request.AdminUserRequest;
import com.portfolio.dto.response.AdminUserResponse;
import com.portfolio.entity.AdminUser;
import com.portfolio.enums.Role;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.mapper.AdminUserMapper;
import com.portfolio.repository.AdminUserRepository;
import com.portfolio.service.impl.AdminUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private AdminUserMapper adminUserMapper = new AdminUserMapper();

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private AdminUser sampleAdmin;

    @BeforeEach
    void setUp() {
        sampleAdmin = AdminUser.builder()
                .id("uuid-admin-1")
                .fullName("Admin User")
                .email("admin@portfolio.com")
                .password("$2a$10$encodedPasswordHash")
                .role(Role.ADMIN)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should create admin successfully with encrypted password")
    void createAdmin_Success() {
        AdminUserRequest request = AdminUserRequest.builder()
                .fullName("New Admin")
                .email("newadmin@portfolio.com")
                .password("SecurePass123!")
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        when(adminUserRepository.existsByEmailIgnoreCase("newadmin@portfolio.com")).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123!")).thenReturn("$2a$10$newHashedPassword");
        when(adminUserRepository.save(any(AdminUser.class))).thenAnswer(invocation -> {
            AdminUser saved = invocation.getArgument(0);
            saved.setId("uuid-admin-2");
            return saved;
        });

        AdminUserResponse response = adminUserService.createAdmin(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("newadmin@portfolio.com");
        verify(passwordEncoder).encode("SecurePass123!");
        verify(adminUserRepository).save(any(AdminUser.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on duplicate admin email")
    void createAdmin_DuplicateEmail_ThrowsException() {
        AdminUserRequest request = AdminUserRequest.builder()
                .email("admin@portfolio.com")
                .password("Password123")
                .build();

        when(adminUserRepository.existsByEmailIgnoreCase("admin@portfolio.com")).thenReturn(true);

        assertThatThrownBy(() -> adminUserService.createAdmin(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should update password when current password matches")
    void updatePassword_Success() {
        AdminPasswordUpdateRequest request = AdminPasswordUpdateRequest.builder()
                .currentPassword("OldPass")
                .newPassword("NewSecurePass")
                .build();

        when(adminUserRepository.findById("uuid-admin-1")).thenReturn(Optional.of(sampleAdmin));
        when(passwordEncoder.matches("OldPass", sampleAdmin.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("NewSecurePass")).thenReturn("$2a$10$updatedHashed");

        adminUserService.updatePassword("uuid-admin-1", request);

        assertThat(sampleAdmin.getPassword()).isEqualTo("$2a$10$updatedHashed");
        verify(adminUserRepository).save(sampleAdmin);
    }

    @Test
    @DisplayName("Should throw BadRequestException when current password does not match")
    void updatePassword_WrongCurrentPassword_ThrowsException() {
        AdminPasswordUpdateRequest request = AdminPasswordUpdateRequest.builder()
                .currentPassword("WrongPassword")
                .newPassword("NewPass")
                .build();

        when(adminUserRepository.findById("uuid-admin-1")).thenReturn(Optional.of(sampleAdmin));
        when(passwordEncoder.matches("WrongPassword", sampleAdmin.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> adminUserService.updatePassword("uuid-admin-1", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Current password does not match");
    }
}
