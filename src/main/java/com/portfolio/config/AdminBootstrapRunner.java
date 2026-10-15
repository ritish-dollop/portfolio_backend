package com.portfolio.config;

import com.portfolio.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements CommandLineRunner {

    private final AdminUserService adminUserService;

    @Value("${app.admin.bootstrap.enabled:true}")
    private boolean bootstrapEnabled;

    @Value("${app.admin.bootstrap.email:admin@portfolio.com}")
    private String bootstrapEmail;

    @Value("${app.admin.bootstrap.password:Admin@123456}")
    private String bootstrapPassword;

    @Value("${app.admin.bootstrap.full-name:Portfolio Administrator}")
    private String bootstrapFullName;

    @Override
    public void run(String... args) {
        if (bootstrapEnabled) {
            try {
                adminUserService.bootstrapInitialAdmin(bootstrapEmail, bootstrapPassword, bootstrapFullName);
            } catch (Exception e) {
                log.error("Failed to bootstrap administrator account", e);
            }
        }
    }
}
