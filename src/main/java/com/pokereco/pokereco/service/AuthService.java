package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.SignInDto;
import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.model.User;
import com.pokereco.pokereco.repository.TokenRepository;
import com.pokereco.pokereco.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthService(final UserRepository userRepository, final TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public SignInDto signIn() {
        UUID userKey = UUID.randomUUID();
        User user = new User(userKey);
        userRepository.save(user);

        UUID accessToken = UUID.randomUUID();
        UUID refreshToken = UUID.randomUUID();

        Token token = new Token(user, accessToken, refreshToken);
        tokenRepository.save(token);

        SignInDto response  = new SignInDto(token.getUser().getId(), token.getAccessToken(), token.getRefreshToken());

        return response;
    }

    @Transactional
    public SignInDto refreshToken(String refreshToken) {
        Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(UUID.fromString(refreshToken));
        if (tokenOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Token token = tokenOptional.get();
        final UUID newAccessToken = UUID.randomUUID();
        final UUID newRefreshToken = UUID.randomUUID();
        Token newToken = new Token(token.getUser(), newAccessToken, newRefreshToken);
        tokenRepository.save(newToken);
        SignInDto response = new SignInDto(token.getUser().getId(), newToken.getAccessToken(), newToken.getRefreshToken());
        return response;
    }
}
