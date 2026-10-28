package com.portfolio.config;

import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.service.NotificationSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(20)
@RequiredArgsConstructor
public class NotificationSeedRunner implements CommandLineRunner {

    private final ContactMessageRepository contactMessageRepository;
    private final NotificationSocketService notificationSocketService;

    @Value("${app.notifications.seed-on-startup:true}")
    private boolean seedOnStartup;

    @Override
    public void run(String... args) {
        if (!seedOnStartup) {
            log.info("Notification seeding on startup is disabled.");
            return;
        }

        try {
            long existingCount = contactMessageRepository.count();
            if (existingCount == 0) {
                log.info("Contact messages table is empty. Seeding initial notifications for testing...");
                notificationSocketService.seedSampleNotifications();
                log.info("Successfully seeded initial contact messages and notifications.");
            } else {
                log.info("Contact messages table already contains {} records. Skipping startup seed.", existingCount);
            }
        } catch (Exception e) {
            log.warn("Could not seed notifications on startup: {}", e.getMessage());
        }
    }
}
