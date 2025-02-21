package com.pokereco.pokereco.service;

import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.model.User;
import com.pokereco.pokereco.repository.TokenRepository;
import com.pokereco.pokereco.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Token signin() {
        String userKey = UUID.randomUUID().toString();
        User user = new User(userKey);
        userRepository.save(user);

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        Token token = new Token(user, accessToken, refreshToken);
        tokenRepository.save(token);

        return token;
    }
}
