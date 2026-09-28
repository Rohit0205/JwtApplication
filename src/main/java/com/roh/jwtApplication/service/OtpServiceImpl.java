package com.roh.jwtApplication.service;

import com.roh.jwtApplication.CustomExceptionHandler.OtpException;
import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.OtpLoginRequestDto;
import com.roh.jwtApplication.dtos.OtpLoginResponseDto;
import com.roh.jwtApplication.dtos.OtpVerifyRequestDto;
import com.roh.jwtApplication.entities.Otp;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.jwtService.JwtService;
import com.roh.jwtApplication.jwtService.RefreshTokenService;
import com.roh.jwtApplication.repository.OtpRepository;
import com.roh.jwtApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class OtpServiceImpl implements OtpService {

    private static final String PURPOSE_LOGIN = "LOGIN";

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final OtpDeliveryService otpDeliveryService;

    @Value("${app.otp.length:6}")
    private int otpLength;

    @Value("${app.otp.expiry-minutes:5}")
    private int expiryMinutes;

    @Value("${app.otp.max-attempts:5}")
    private int maxAttempts;

    @Value("${app.otp.expose-in-response:true}")
    private boolean exposeOtpInResponse;

    private final SecureRandom secureRandom = new SecureRandom();

    public OtpServiceImpl(UserRepository userRepository,
                          OtpRepository otpRepository,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          OtpDeliveryService otpDeliveryService) {

        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.otpDeliveryService = otpDeliveryService;
    }

    @Override
    @Transactional
    public OtpLoginResponseDto requestOtp(OtpLoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new OtpException(
                        HttpStatus.NOT_FOUND,
                        "No account found with email: " + request.getEmail()
                ));

        LocalDateTime now = LocalDateTime.now();

        String otpCode = generateOtp();

        // Invalidate any previously generated, still-active OTP for this user
        otpRepository.revokeActiveOtps(user.getId(), PURPOSE_LOGIN, now);

        Otp otp = new Otp();
        otp.setUser(user);
        otp.setOtpHash(hashToken(otpCode));
        otp.setPurpose(PURPOSE_LOGIN);
        otp.setExpiresAt(now.plusMinutes(expiryMinutes));
        otp.setMaxAttempts(maxAttempts);

        otpRepository.save(otp);

        otpDeliveryService.sendOtp(user.getEmail(), otpCode);

        return new OtpLoginResponseDto(
                "OTP sent successfully to " + maskEmail(user.getEmail()),
                expiryMinutes * 60,
                exposeOtpInResponse ? otpCode : null
        );
    }

    @Override
    @Transactional
    public LoginResponseDto verifyOtpAndLogin(OtpVerifyRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new OtpException(
                        HttpStatus.NOT_FOUND,
                        "No account found with email: " + request.getEmail()
                ));

        LocalDateTime now = LocalDateTime.now();

        Otp otp = otpRepository
                .findFirstByUser_IdAndPurposeAndRevokedFalseAndVerifiedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                        user.getId(), PURPOSE_LOGIN, now)
                .orElseThrow(() -> new OtpException(
                        HttpStatus.BAD_REQUEST,
                        "OTP not found or expired. Please request a new OTP."
                ));

        // Reached max failed attempts -> force the user to request a fresh OTP
        if (otp.getAttempts() >= otp.getMaxAttempts()) {
            throw new OtpException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many failed OTP attempts. Please request a new OTP."
            );
        }

        if (!hashToken(request.getOtp()).equals(otp.getOtpHash())) {
            // Increment in its own transaction so it is not rolled back
            // when the OtpException below aborts the outer transaction.
            otpRepository.incrementAttempts(otp.getId());

            throw new OtpException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid OTP. Please check and try again."
            );
        }

        // Valid OTP -> mark it as used
        otp.setVerified(true);
        otp.setVerifiedAt(now);
        otpRepository.save(otp);

        // Email ownership is now proven
        user.setEmailVerified(true);
        user.setLastLoginAt(now);
        userRepository.save(user);

        // Issue JWT tokens (same as password login)
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponseDto(accessToken, "Bearer", refreshToken);
    }

    private String generateOtp() {

        StringBuilder sb = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            sb.append(secureRandom.nextInt(10));
        }

        return sb.toString();
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }

    private String maskEmail(String email) {

        int at = email.indexOf('@');

        if (at <= 1) {
            return email;
        }

        return email.substring(0, 1) + "****" + email.substring(at);
    }
}