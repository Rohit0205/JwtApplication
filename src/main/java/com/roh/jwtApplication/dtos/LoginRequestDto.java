package com.roh.jwtApplication.dtos;

public class LoginRequestDto {

    private String emailId;
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
