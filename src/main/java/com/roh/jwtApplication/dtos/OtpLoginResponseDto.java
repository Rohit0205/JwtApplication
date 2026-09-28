package com.roh.jwtApplication.dtos;

public class OtpLoginResponseDto {

    private String message;
    private int expiresInSeconds;

    /**
     * Returned only when {@code app.otp.expose-in-response=true} (dev/demo).
     * In production keep this disabled and rely on email/SMS delivery.
     */
    private String otp;

    public OtpLoginResponseDto() {
    }

    public OtpLoginResponseDto(String message, int expiresInSeconds, String otp) {
        this.message = message;
        this.expiresInSeconds = expiresInSeconds;
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(int expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}