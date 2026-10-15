package com.portfolio.service;

import com.portfolio.dto.request.AdminPasswordUpdateRequest;
import com.portfolio.dto.request.AdminUserRequest;
import com.portfolio.dto.response.AdminUserResponse;

public interface AdminUserService {

    AdminUserResponse findByEmail(String email);

    AdminUserResponse getAdminById(String id);

    AdminUserResponse createAdmin(AdminUserRequest request);

    AdminUserResponse updateAdmin(String id, AdminUserRequest request);

    AdminUserResponse updateStatus(String id, boolean enabled);

    void updatePassword(String id, AdminPasswordUpdateRequest request);

    void bootstrapInitialAdmin(String email, String password, String fullName);
}
