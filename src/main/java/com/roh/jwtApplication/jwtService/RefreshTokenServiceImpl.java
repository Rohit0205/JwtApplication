package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.CustomExceptionHandler.RefreshTokenException;
import com.roh.jwtApplication.entities.RefreshToken;
import com.roh.jwtApplication.entities.User;
import com.roh.jwtApplication.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

 private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String createRefreshToken(User user) {

        String rawToken = generateRefreshToken();

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }
    @Override
    public RefreshToken verifyRefreshToken(String token) {

        String tokenHash = hashToken(token);
        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new RefreshTokenException(
                                        "Refresh token not found"
                                )
                        );

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


    private String generateRefreshToken() {

        byte[] randomBytes = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }
}
