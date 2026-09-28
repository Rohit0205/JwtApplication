package com.roh.jwtApplication.service;

import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.OtpLoginRequestDto;
import com.roh.jwtApplication.dtos.OtpLoginResponseDto;
import com.roh.jwtApplication.dtos.OtpVerifyRequestDto;

public interface OtpService {

    /**
     * Generates an OTP for an existing user and delivers it (email / console).
     * Any previously generated, still-active OTP for the same user is invalidated.
     */
    OtpLoginResponseDto requestOtp(OtpLoginRequestDto request);

    /**
     * Verifies the submitted OTP. On success, issues JWT access + refresh tokens.
     */
    LoginResponseDto verifyOtpAndLogin(OtpVerifyRequestDto request);
}