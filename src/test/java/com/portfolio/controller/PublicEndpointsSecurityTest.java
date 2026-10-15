package com.portfolio.controller;

import com.portfolio.constant.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicEndpointsSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Public projects endpoint should be accessible without credentials")
    void publicProjectsEndpoint_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get(ApiConstants.PUBLIC_PROJECTS + ApiConstants.GET_PUBLISHED_PROJECTS))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public skills endpoint should be accessible without credentials")
    void publicSkillsEndpoint_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get(ApiConstants.PUBLIC_SKILLS + ApiConstants.GET_SKILLS))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin projects endpoint should require authentication (401)")
    void adminProjectsEndpoint_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get(ApiConstants.ADMIN_PROJECTS + ApiConstants.GET_ALL_PROJECTS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Admin profile endpoint should require authentication (401)")
    void adminProfileEndpoint_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get(ApiConstants.ADMIN_PROFILE + ApiConstants.GET_ADMIN_PROFILE))
                .andExpect(status().isUnauthorized());
    }
}
