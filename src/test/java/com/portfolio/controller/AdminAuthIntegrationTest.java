package com.portfolio.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.constant.ApiConstants;
import com.portfolio.dto.request.LoginRequest;
import com.portfolio.dto.request.RefreshTokenRequest;
import com.portfolio.entity.AdminUser;
import com.portfolio.enums.Role;
import com.portfolio.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "admin_auth_test@portfolio.com";
    private static final String TEST_PASSWORD = "Password@123";

    @BeforeEach
    void setUp() {
        adminUserRepository.findByEmailIgnoreCase(TEST_EMAIL).ifPresent(adminUserRepository::delete);

        AdminUser user = AdminUser.builder()
                .fullName("Test Administrator")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .role(Role.ADMIN)
                .enabled(true)
                .build();
        adminUserRepository.save(user);
    }

    @Test
    @DisplayName("Login should return both access and refresh tokens")
    void login_ShouldReturnAccessAndRefreshTokens() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        mockMvc.perform(post(ApiConstants.API_ADMIN_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").isNumber())
                .andExpect(jsonPath("$.data.refreshExpiresIn").isNumber())
                .andExpect(jsonPath("$.data.user.email").value(TEST_EMAIL));
    }

    @Test
    @DisplayName("Refresh token endpoint should issue new tokens when given valid refresh token")
    void refreshToken_WithValidToken_ShouldIssueNewTokens() throws Exception {
        // Step 1: Login to get tokens
        LoginRequest loginRequest = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        MvcResult loginResult = mockMvc.perform(post(ApiConstants.API_ADMIN_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode loginJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String initialRefreshToken = loginJson.get("data").get("refreshToken").asText();

        // Step 2: Call refreshToken endpoint
        RefreshTokenRequest refreshRequest = RefreshTokenRequest.builder()
                .refreshToken(initialRefreshToken)
                .build();

        mockMvc.perform(post(ApiConstants.API_ADMIN_AUTH_REFRESH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("Refresh token endpoint should reject invalid refresh token with 401")
    void refreshToken_WithInvalidToken_ShouldReturn401() throws Exception {
        RefreshTokenRequest invalidRequest = RefreshTokenRequest.builder()
                .refreshToken("invalid.jwt.token")
                .build();

        mockMvc.perform(post(ApiConstants.API_ADMIN_AUTH_REFRESH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Using refresh token as Bearer authorization header should be rejected")
    void bearerHeader_UsingRefreshToken_ShouldBeRejected() throws Exception {
        // Step 1: Login to get refresh token
        LoginRequest loginRequest = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        MvcResult loginResult = mockMvc.perform(post(ApiConstants.API_ADMIN_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode loginJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String refreshToken = loginJson.get("data").get("refreshToken").asText();

        // Step 2: Try to access protected admin endpoint using refresh token in Bearer header
        mockMvc.perform(get(ApiConstants.ADMIN_PROFILE + ApiConstants.GET_ADMIN_PROFILE)
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isUnauthorized());
    }
}
