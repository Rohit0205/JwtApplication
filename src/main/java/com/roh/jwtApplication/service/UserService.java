package com.roh.jwtApplication.service;

import com.roh.jwtApplication.dtos.LoginRequestDto;
import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.RegisterRequestDto;
import com.roh.jwtApplication.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public void registerUser(RegisterRequestDto request);
    public LoginResponseDto login(LoginRequestDto request);

}
