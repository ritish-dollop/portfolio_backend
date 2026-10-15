package com.portfolio.service;

import com.portfolio.dto.request.ProfileRequest;
import com.portfolio.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getPublicProfile();

    ProfileResponse getAdminProfile();

    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse updateProfile(ProfileRequest request);
}
