package com.authapp.repository;

import com.authapp.entity.RefreshToken;
import com.authapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByToken(String token);
    void deleteByUserId(Integer userId);
}