package com.roh.jwtApplication.dtos;

public class LoginResponseDto {
    private String accessToken;
    private String TokenType;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        TokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return TokenType;
    }

    public void setTokenType(String tokenType) {
        TokenType = tokenType;
    }
}
