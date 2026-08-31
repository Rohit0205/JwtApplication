package com.roh.jwtApplication.service;

import com.roh.jwtApplication.dtos.LoginRequestDto;
import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.RefreshTokenRequestDto;
import com.roh.jwtApplication.dtos.RegisterRequestDto;
import com.roh.jwtApplication.entities.RefreshToken;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.jwtService.JwtService;
import com.roh.jwtApplication.jwtService.RefreshTokenService;
import com.roh.jwtApplication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void registerUser(RegisterRequestDto request) {

        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());

        // Hash password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmailId()).orElseThrow(()->   new IllegalArgumentException("user Not Found"));

        boolean passwordMatches =passwordEncoder.matches(request.getPassword(),user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }
        // Generate access token
        String NewAccessToken =
                jwtService.generateToken(user);

        // Generate refresh token
        String newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponseDto(
                NewAccessToken,
                "Bearer",
                newRefreshToken
        );

      //  return new LoginResponseDto(accessToken, "Bearer", refreshToken.getToken());

    }


    public LoginResponseDto refreshAccessToken(
            RefreshTokenRequestDto request) {

        // 1. Validate old refresh token
        RefreshToken oldRefreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        // 2. Get user
        User user = oldRefreshToken.getUser();

        // 3. Revoke old refresh token
        refreshTokenService.revokeToken(oldRefreshToken);

        // 4. Generate new access token
        String newAccessToken = jwtService.generateToken(user);

        // 5. Generate new refresh token
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        // 6. Return both
        return new LoginResponseDto(newAccessToken, "Bearer", newRefreshToken);
    }

    @Override
    public void logout(RefreshTokenRequestDto request) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken()
                );

        refreshTokenService.revokeToken(refreshToken);
    }
}
