package com.roh.jwtApplication.service;

/**
 * Abstraction over the OTP delivery channel.
 * Current implementation: EmailOtpDeliveryService.
 * Future implementation (SMS): add an SmsOtpDeliveryService.
 */
public interface OtpDeliveryService {

    /**
     * Deliver an OTP code to the given email address.
     *
     * @param email   destination email
     * @param otpCode generated OTP code
     */
    void sendOtp(String email, String otpCode);
}