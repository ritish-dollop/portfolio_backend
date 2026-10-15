package com.portfolio.service.impl;

import com.portfolio.dto.request.AdminPasswordUpdateRequest;
import com.portfolio.dto.request.AdminUserRequest;
import com.portfolio.dto.response.AdminUserResponse;
import com.portfolio.entity.AdminUser;
import com.portfolio.enums.Role;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.DuplicateResourceException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.AdminUserMapper;
import com.portfolio.repository.AdminUserRepository;
import com.portfolio.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse findByEmail(String email) {
        AdminUser admin = adminUserRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "email", email));
        return adminUserMapper.toResponse(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse getAdminById(String id) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id));
        return adminUserMapper.toResponse(admin);
    }

    @Override
    @Transactional
    public AdminUserResponse createAdmin(AdminUserRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (adminUserRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("AdminUser", "email", email);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        AdminUser admin = adminUserMapper.toEntity(request, encodedPassword);
        AdminUser saved = adminUserRepository.save(admin);
        log.info("Created new administrator account for: {}", saved.getEmail());
        return adminUserMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AdminUserResponse updateAdmin(String id, AdminUserRequest request) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id));

        String email = request.getEmail().trim().toLowerCase();
        if (adminUserRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateResourceException("AdminUser", "email", email);
        }

        adminUserMapper.updateEntity(admin, request);
        AdminUser updated = adminUserRepository.save(admin);
        log.info("Updated administrator details for ID: {}", id);
        return adminUserMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public AdminUserResponse updateStatus(String id, boolean enabled) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id));

        admin.setEnabled(enabled);
        AdminUser updated = adminUserRepository.save(admin);
        log.info("Updated administrator active status to {} for ID: {}", enabled, id);
        return adminUserMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void updatePassword(String id, AdminPasswordUpdateRequest request) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id));

        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new BadRequestException("Current password does not match");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminUserRepository.save(admin);
        log.info("Password updated successfully for administrator ID: {}", id);
    }

    @Override
    @Transactional
    public void bootstrapInitialAdmin(String email, String password, String fullName) {
        String normalizedEmail = email.trim().toLowerCase();
        if (!adminUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            AdminUser bootstrapAdmin = AdminUser.builder()
                    .fullName(fullName)
                    .email(normalizedEmail)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            adminUserRepository.save(bootstrapAdmin);
            log.info("Initial bootstrap administrator account created: {}", normalizedEmail);
        }
    }
}
