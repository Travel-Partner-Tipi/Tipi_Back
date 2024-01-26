package com.app.repository;

import com.app.entitiy.RfToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RfToken, Long> {
    Optional<RfToken> findByUserId(String userId);
    Optional<RfToken> findByRefreshToken(String refreshToken);
}