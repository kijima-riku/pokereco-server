package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.response.AuthResponseDto;
import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.model.User;
import com.pokereco.pokereco.repository.TokenRepository;
import com.pokereco.pokereco.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  AuthService(final UserRepository userRepository, final TokenRepository tokenRepository) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
  }

  @Transactional
  public AuthResponseDto signIn() {
    UUID userKey = UUID.randomUUID();
    User user = new User(userKey);
    userRepository.save(user);

    UUID accessToken = UUID.randomUUID();
    UUID refreshToken = UUID.randomUUID();

    Token token = new Token(user, accessToken, refreshToken);
    tokenRepository.save(token);

    return new AuthResponseDto(
        token.getUser().getId(), token.getAccessToken(), token.getRefreshToken());
  }

  @Transactional
  public AuthResponseDto refreshToken(String refreshToken) {
    Optional<Token> tokenOptional =
        tokenRepository.findByRefreshToken(UUID.fromString(refreshToken));
    if (tokenOptional.isEmpty()) {
      throw new IllegalArgumentException("Invalid refresh token");
    }
    Token token = tokenOptional.get();
    final UUID newAccessToken = UUID.randomUUID();
    final UUID newRefreshToken = UUID.randomUUID();
    Token newToken = new Token(token.getUser(), newAccessToken, newRefreshToken);
    tokenRepository.save(newToken);
    return new AuthResponseDto(
        token.getUser().getId(), newToken.getAccessToken(), newToken.getRefreshToken());
  }
}
