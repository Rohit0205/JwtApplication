package com.roh.jwtApplication.controller;

import com.roh.jwtApplication.dtos.LoginRequestDto;
import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.OtpLoginRequestDto;
import com.roh.jwtApplication.dtos.OtpLoginResponseDto;
import com.roh.jwtApplication.dtos.OtpVerifyRequestDto;
import com.roh.jwtApplication.dtos.RefreshTokenRequestDto;
import com.roh.jwtApplication.dtos.RegisterRequestDto;
import com.roh.jwtApplication.service.OtpService;
import com.roh.jwtApplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService authService;
    private final OtpService otpService;

    public AuthController(UserService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto requestDto)
    {
        authService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {

        LoginResponseDto response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {

        LoginResponseDto response = authService.refreshAccessToken(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/send")
    public ResponseEntity<OtpLoginResponseDto> sendOtp(@RequestBody OtpLoginRequestDto request) {

        OtpLoginResponseDto response = otpService.requestOtp(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<LoginResponseDto> verifyOtp(@RequestBody OtpVerifyRequestDto request) {

        LoginResponseDto response = otpService.verifyOtpAndLogin(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenRequestDto request) {

        authService.logout(request);

        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/test")
    public String test() {
        return "JWT authentication successful";
    }
}
