package com.authapp.service.i;

import com.authapp.entity.RefreshToken;
import com.authapp.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String username);
    public RefreshToken verifyExpiration(RefreshToken token);
}
