package com.roh.jwtApplication.service;

import com.roh.jwtApplication.dtos.*;
import com.roh.jwtApplication.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public void registerUser(RegisterRequestDto request);
    public LoginResponseDto login(LoginRequestDto request);
    public LoginResponseDto refreshAccessToken(RefreshTokenRequestDto request);
    public void logout(RefreshTokenRequestDto request);
}
