package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.enums.Role;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final Role role;
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(
            Long userId,
            String email,
            Role role,
            String password,
            Collection<? extends GrantedAuthority> authorities) {

        this.userId = userId;
        this.email = email;
        this.role = role;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}