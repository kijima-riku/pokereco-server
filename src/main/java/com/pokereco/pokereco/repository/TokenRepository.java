package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(UUID accessToken);
    Optional<Token> findByRefreshToken(UUID refreshToken);

}
