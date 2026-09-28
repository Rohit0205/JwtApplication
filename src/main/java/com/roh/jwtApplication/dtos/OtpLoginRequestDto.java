package com.roh.jwtApplication.dtos;

public class OtpLoginRequestDto {

    private String email;

    public OtpLoginRequestDto() {
    }

    public OtpLoginRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}