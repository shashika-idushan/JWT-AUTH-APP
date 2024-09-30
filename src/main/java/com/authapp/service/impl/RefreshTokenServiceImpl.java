package com.authapp.service.impl;

import com.authapp.entity.RefreshToken;
import com.authapp.entity.User;
import com.authapp.repository.RefreshTokenRepository;
import com.authapp.repository.UserRepository;
import com.authapp.service.i.RefreshTokenService;
import com.authapp.enums.CommonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsernameAndStatus(username, CommonStatus.ACTIVE).get();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }
}
