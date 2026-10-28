package com.portfolio.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("================================================================================");
        log.info(">>>>> [PORTFOLIO CMS OTP] Admin Login OTP for {}: {} (Valid for 5 mins) <<<<<", toEmail, otp);
        log.info("================================================================================");

        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
            log.info("SMTP JavaMailSender not configured. OTP printed to server logs for testing: {}", otp);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your Admin Login One-Time Passcode (OTP)");

            String html = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 12px; background: #ffffff;">
                      <h2 style="color: #6366f1; margin-bottom: 8px;">Portfolio CMS Administrator Login</h2>
                      <p style="color: #475569; font-size: 15px;">You requested a one-time passcode to sign into your administrator dashboard.</p>
                      <div style="margin: 28px 0; text-align: center;">
                        <span style="display: inline-block; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #4338ca; background: #eef2ff; padding: 12px 28px; border-radius: 8px; border: 1px dashed #6366f1;">
                          %s
                        </span>
                      </div>
                      <p style="color: #64748b; font-size: 13px;">This code will expire in <strong>5 minutes</strong>. If you did not request this login code, please ignore this email or change your password immediately.</p>
                    </div>
                    """.formatted(otp);

            log.info("Attempting to dispatch email from {} to {} via SMTP", fromEmail, toEmail);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Successfully sent OTP email to: {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send email via SMTP to {}: {}. Note: OTP is available in logs.", toEmail, e.getMessage(), e);
        }
    }
}
