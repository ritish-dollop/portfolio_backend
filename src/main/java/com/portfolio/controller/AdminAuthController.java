package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import com.portfolio.constant.AppConstants;
import com.portfolio.dto.request.AdminPasswordUpdateRequest;
import com.portfolio.dto.request.LoginRequest;
import com.portfolio.dto.request.OtpLoginRequest;
import com.portfolio.dto.request.RefreshTokenRequest;
import com.portfolio.dto.request.SendOtpRequest;
import com.portfolio.dto.response.AdminUserResponse;
import com.portfolio.dto.response.ApiResponse;
import com.portfolio.dto.response.JwtAuthResponse;
import com.portfolio.exception.UnauthorizedException;
import com.portfolio.security.AdminPrincipal;
import com.portfolio.security.CustomUserDetailsService;
import com.portfolio.security.JwtTokenProvider;
import com.portfolio.service.AdminUserService;
import com.portfolio.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ADMIN_AUTH)
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminUserService adminUserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OtpService otpService;

    @PostMapping(ApiConstants.LOGIN)
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().trim().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        AdminPrincipal principal = (AdminPrincipal) authentication.getPrincipal();
        AdminUserResponse userResponse = adminUserService.getAdminById(principal.getId());

        JwtAuthResponse authResponse = JwtAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(AppConstants.TOKEN_TYPE)
                .expiresIn(tokenProvider.getExpirationInMs())
                .refreshExpiresIn(tokenProvider.getRefreshExpirationInMs())
                .user(userResponse)
                .build();

        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_LOGIN_SUCCESS, authResponse));
    }

    @PostMapping(ApiConstants.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<JwtAuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String username = tokenProvider.getUsernameFromJwt(refreshToken);
        if (username == null) {
            throw new UnauthorizedException("Unable to extract identity from refresh token");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (!userDetails.isEnabled()) {
            throw new UnauthorizedException("Administrator account is disabled");
        }

        String newAccessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = tokenProvider.generateRefreshToken(userDetails);

        AdminUserResponse userResponse = adminUserService.findByEmail(username);

        JwtAuthResponse authResponse = JwtAuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType(AppConstants.TOKEN_TYPE)
                .expiresIn(tokenProvider.getExpirationInMs())
                .refreshExpiresIn(tokenProvider.getRefreshExpirationInMs())
                .user(userResponse)
                .build();

        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_TOKEN_REFRESH_SUCCESS, authResponse));
    }

    @GetMapping(ApiConstants.GET_CURRENT_USER)
    public ResponseEntity<ApiResponse<AdminUserResponse>> getCurrentUser(@AuthenticationPrincipal AdminPrincipal principal) {
        AdminUserResponse response = adminUserService.getAdminById(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping(ApiConstants.CHANGE_PASSWORD)
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody AdminPasswordUpdateRequest request) {
        adminUserService.updatePassword(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_PASSWORD_CHANGED_SUCCESS, null));
    }

    @PostMapping(ApiConstants.SEND_OTP)
    public ResponseEntity<ApiResponse<Void>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP has been sent to your email address", null));
    }

    @PostMapping(ApiConstants.LOGIN_WITH_OTP)
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginWithOtp(@Valid @RequestBody OtpLoginRequest request) {
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!valid) {
            throw new UnauthorizedException("Invalid or expired OTP code");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail().trim().toLowerCase());
        if (!userDetails.isEnabled()) {
            throw new UnauthorizedException("Administrator account is disabled");
        }

        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        AdminUserResponse userResponse = adminUserService.findByEmail(request.getEmail().trim().toLowerCase());

        JwtAuthResponse authResponse = JwtAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(AppConstants.TOKEN_TYPE)
                .expiresIn(tokenProvider.getExpirationInMs())
                .refreshExpiresIn(tokenProvider.getRefreshExpirationInMs())
                .user(userResponse)
                .build();

        return ResponseEntity.ok(ApiResponse.success(AppConstants.MSG_LOGIN_SUCCESS, authResponse));
    }
}
