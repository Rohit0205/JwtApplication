package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.entities.RolePermission;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.repository.RolePermissionRepository;
import com.roh.jwtApplication.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            RolePermissionRepository rolePermissionRepository) {

        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1. Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );
        System.err.println("User Object>>>>>>"+user.toString());
        System.err.println("role Object>>>>>>"+user.getRole());
        // 2. Get permissions assigned to user's role
        List<RolePermission> rolePermissions =
                rolePermissionRepository.findByRoleId(
                        user.getRole().getId()
                );

        // 3. Create authorities
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Role authority
        authorities.add(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().getName()
                )
        );

        // Permission authorities
        rolePermissions.stream()
                .filter(rp -> rp.getStatus() == 'Y')
                .filter(rp -> rp.getPermission().getStatus() == 'Y')
                .map(rp ->
                        new SimpleGrantedAuthority(
                                rp.getPermission().getName()
                        )
                )
                .forEach(authorities::add);

        // 4. Return custom principal
        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getPassword(),
                authorities
        );
    }
}