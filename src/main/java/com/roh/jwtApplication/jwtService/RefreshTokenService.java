package com.roh.jwtApplication.jwtService;

import com.roh.jwtApplication.entities.RefreshToken;
import com.roh.jwtApplication.entities.User;

public interface RefreshTokenService {

    public String createRefreshToken(User user);
    public RefreshToken verifyRefreshToken(String token);
    public void revokeToken(RefreshToken refreshToken);

}
