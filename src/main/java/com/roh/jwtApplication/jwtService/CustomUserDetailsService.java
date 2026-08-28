package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Role
        authorities.add(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                )
        );

        // Permissions
        user.getRole()
                .getPermissions()
                .forEach(permission ->
                        authorities.add(
                                new SimpleGrantedAuthority(
                                        permission.name()
                                )
                        )
                );

        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getPassword(),
                authorities
        );
    }
}
