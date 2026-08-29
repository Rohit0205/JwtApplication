package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.CustomExceptionHandler.RefreshTokenException;
import com.roh.jwtApplication.entities.RefreshToken;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

 private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RefreshTokenException(
                                        "Refresh token not found"
                                ));

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeToken(RefreshToken refreshToken) {

        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshToken);
    }
}
