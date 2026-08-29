package com.roh.jwtApplication.dtos;

public class LoginResponseDto {
    private String accessToken;
    private String TokenType;
    private String refreshToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String accessToken, String tokenType, String refreshToken) {
        this.accessToken = accessToken;
        TokenType = tokenType;
        this.refreshToken = refreshToken;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
