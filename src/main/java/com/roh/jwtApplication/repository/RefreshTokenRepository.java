package com.roh.jwtApplication.repository;

import com.roh.jwtApplication.entities.RefreshToken;
import com.roh.jwtApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

}
