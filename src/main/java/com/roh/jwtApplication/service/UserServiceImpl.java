package com.roh.jwtApplication.service;

import com.roh.jwtApplication.dtos.LoginRequestDto;
import com.roh.jwtApplication.dtos.LoginResponseDto;
import com.roh.jwtApplication.dtos.RegisterRequestDto;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.jwtService.JwtService;
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


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
        String token = jwtService.generateToken(user);
        return new LoginResponseDto(token, "Bearer");

    }


}
