package com.portfolio.service;

import com.portfolio.dto.event.OtpEvent;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.AdminUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OtpService {

    private static final String REDIS_OTP_PREFIX = "admin:otp:";
    private static final long OTP_VALIDITY_MINUTES = 5;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;
    private final AdminUserRepository adminUserRepository;

    @Value("${app.kafka.enabled:true}")
    private boolean kafkaEnabled;

    @Value("${app.kafka.otp-topic:admin-otp-topic}")
    private String otpTopic;

    // Resilient local fallback cache in case Redis is offline during local development
    private final Map<String, LocalOtpRecord> localOtpFallback = new ConcurrentHashMap<>();

    private record LocalOtpRecord(String otp, long expiryTimeMillis) {}

    public OtpService(
            @Autowired(required = false) KafkaTemplate<String, Object> kafkaTemplate,
            @Autowired(required = false) RedisTemplate<String, Object> redisTemplate,
            EmailService emailService,
            AdminUserRepository adminUserRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.adminUserRepository = adminUserRepository;
    }

    public void sendOtp(String email) {
        String cleanEmail = email.trim().toLowerCase();

        // 1. Verify that admin account exists with this email
        adminUserRepository.findByEmailIgnoreCase(cleanEmail)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "email", cleanEmail));

        // 2. Generate secure 6-digit numeric OTP
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        // 3. Store OTP in Redis (with in-memory fallback if Redis is unavailable)
        storeOtp(cleanEmail, otp);

        // 4. Publish to Kafka topic for asynchronous processing
        OtpEvent event = OtpEvent.builder()
                .email(cleanEmail)
                .otp(otp)
                .build();

        boolean kafkaDispatched = false;
        if (kafkaEnabled && kafkaTemplate != null) {
            try {
                kafkaTemplate.send(otpTopic, cleanEmail, event).get(2, TimeUnit.SECONDS);
                kafkaDispatched = true;
                log.info("Dispatched OTP event to Kafka topic '{}' for: {}", otpTopic, cleanEmail);
            } catch (Exception e) {
                log.warn("Kafka broker unreachable ({}); falling back to direct email dispatch", e.getMessage());
            }
        }

        // 5. Fallback: If Kafka was disabled or unreachable, dispatch email directly
        if (!kafkaDispatched) {
            emailService.sendOtpEmail(cleanEmail, otp);
        }
    }

    @KafkaListener(
            topics = "${app.kafka.otp-topic:admin-otp-topic}",
            groupId = "${spring.kafka.consumer.group-id:portfolio-admin-group}",
            autoStartup = "${spring.kafka.listener.auto-startup:true}"
    )
    public void consumeOtpEvent(OtpEvent event) {
        if (event != null && event.getEmail() != null && event.getOtp() != null) {
            log.info("Received OTP event from Kafka topic '{}' for: {}", otpTopic, event.getEmail());
            emailService.sendOtpEmail(event.getEmail(), event.getOtp());
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String cleanEmail = email.trim().toLowerCase();
        String storedOtp = getStoredOtp(cleanEmail);

        if (storedOtp != null && storedOtp.equals(otp.trim())) {
            removeStoredOtp(cleanEmail);
            log.info("OTP verified successfully for administrator: {}", cleanEmail);
            return true;
        }

        log.warn("OTP verification failed for administrator: {}", cleanEmail);
        return false;
    }

    public String getStoredOtpForTesting(String email) {
        return getStoredOtp(email.trim().toLowerCase());
    }

    private void storeOtp(String email, String otp) {
        String key = REDIS_OTP_PREFIX + email;
        boolean storedInRedis = false;

        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(OTP_VALIDITY_MINUTES));
                storedInRedis = true;
                log.info("Stored OTP in Redis for key: {}", key);
            } catch (Exception e) {
                log.warn("Redis unavailable ({}); storing OTP in local fallback cache", e.getMessage());
            }
        }

        if (!storedInRedis) {
            long expiry = System.currentTimeMillis() + (OTP_VALIDITY_MINUTES * 60 * 1000);
            localOtpFallback.put(email, new LocalOtpRecord(otp, expiry));
            log.info("Stored OTP in local fallback cache for: {}", email);
        }
    }

    private String getStoredOtp(String email) {
        String key = REDIS_OTP_PREFIX + email;

        if (redisTemplate != null) {
            try {
                Object val = redisTemplate.opsForValue().get(key);
                if (val != null) {
                    return val.toString();
                }
            } catch (Exception e) {
                log.warn("Redis read failed ({}); checking local fallback cache", e.getMessage());
            }
        }

        LocalOtpRecord record = localOtpFallback.get(email);
        if (record != null) {
            if (System.currentTimeMillis() <= record.expiryTimeMillis()) {
                return record.otp();
            } else {
                localOtpFallback.remove(email);
            }
        }

        return null;
    }

    private void removeStoredOtp(String email) {
        String key = REDIS_OTP_PREFIX + email;
        if (redisTemplate != null) {
            try {
                redisTemplate.delete(key);
            } catch (Exception ignored) {}
        }
        localOtpFallback.remove(email);
    }
}
