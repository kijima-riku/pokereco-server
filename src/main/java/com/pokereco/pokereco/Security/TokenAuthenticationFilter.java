package com.pokereco.pokereco.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final TokenRepository tokenRepository;
  private static final Integer ACCESS_TOKEN_EXPIRATION = 15;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public TokenAuthenticationFilter(final TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws IOException, ServletException {
    if (request.getServletPath().equals("/api/v1/auth/signIn")
        || request.getServletPath().equals("/api/v1/auth/refresh")) {
      filterChain.doFilter(request, response);
      return;
    }

    UUID accessToken = getAccessTokenFromCookie(request);
    if (accessToken == null) {
      writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "invalid token.");
      return;
    }

    Optional<Token> token = tokenRepository.findByAccessToken(accessToken);
    if (token.isEmpty()) {
      writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token.");
      return;
    }

    if (token
        .get()
        .getCreatedAt()
        .plusMinutes(ACCESS_TOKEN_EXPIRATION)
        .isBefore(LocalDateTime.now())) {
      writeErrorResponse(
          response,
          HttpServletResponse.SC_UNAUTHORIZED,
          "Access token expired. Please refresh token.");
      return;
    }

    Long userId = token.get().getUser().getId();
    CustomUserPrincipal principal = new CustomUserPrincipal(userId);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private UUID getAccessTokenFromCookie(final HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("accessToken".equals(cookie.getName())) {
          try {
            return UUID.fromString(cookie.getValue());
          } catch (IllegalArgumentException e) {
            return null;
          }
        }
      }
    }
    return null;
  }

  private void writeErrorResponse(HttpServletResponse response, int status, String message)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    Map<String, String> errorBody = Collections.singletonMap("message", message);
    String json = objectMapper.writeValueAsString(errorBody);
    response.getWriter().write(json);
    response.getWriter().flush();
  }
}
