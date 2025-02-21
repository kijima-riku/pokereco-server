package com.pokereco.pokereco.Security;

import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;
    final Integer ACCESS_TOKEN_EXPIRATION = 15;

    public TokenAuthenticationFilter(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        if(request.getServletPath().equals("/api/v1/auth/signIn") || request.getServletPath().equals("/api/v1/auth/refresh")){
            filterChain.doFilter(request, response);
            return;
        }
        UUID accessToken = getAccessTokenFromRequest(request);
        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization Header.");
            return;
        }

        Optional<Token> token = tokenRepository.findByAccessToken(accessToken);
        if(token.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid access token");
            return;
        }

        if(token.get().getCreatedAt().plusMinutes(ACCESS_TOKEN_EXPIRATION).isBefore(LocalDateTime.now())){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access token expired. Please refresh token.");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        accessToken,
                        null,
                        Collections.emptyList()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
    }

    private UUID getAccessTokenFromRequest(final HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null){
            return null;
        }
        return UUID.fromString(bearerToken);
    }
}
