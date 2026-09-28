package com.roh.jwtApplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailOtpDeliveryService implements OtpDeliveryService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailOtpDeliveryService.class);

    private final ObjectProvider<JavaMailSender> javaMailSenderProvider;
    private final String from;
    private final int expiresInMinutes;

    public EmailOtpDeliveryService(
            ObjectProvider<JavaMailSender> javaMailSenderProvider,
            @Value("${app.otp.mail.from:no-reply@localhost}") String from,
            @Value("${app.otp.expiry-minutes:5}") int expiresInMinutes) {

        this.javaMailSenderProvider = javaMailSenderProvider;
        this.from = from;
        this.expiresInMinutes = expiresInMinutes;
    }

    @Override
    public void sendOtp(String email, String otpCode) {

        // Always log the OTP so the flow can be tested without an SMTP server
        log.info("================================================================");
        log.info("OTP LOGIN -> email: {} | OTP: {} | valid for {} minutes",
                email, otpCode, expiresInMinutes);
        log.info("================================================================");

        JavaMailSender mailSender = javaMailSenderProvider.getIfAvailable();

        // No SMTP configured -> OTP is only visible in the console logs
        if (mailSender == null) {
            log.warn("No JavaMailSender bean found (set spring.mail.host to enable email delivery). " +
                    "OTP was logged to console only.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("Your OTP Login Code");
            message.setText("Your OTP for login is: " + otpCode +
                    ". It is valid for " + expiresInMinutes +
                    " minutes. Do not share this code.");

            mailSender.send(message);
            log.info("OTP email sent successfully to {}", email);

        } catch (MailException e) {
            log.warn("Failed to send OTP email to {}: {}", email, e.getMessage());
        }
    }
}