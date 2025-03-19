package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.SignInDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;
  private final ResponseBuilder responseBuilder;
  int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 15 minutes
  int REFRESH_TOKEN_EXPIRATION = 90 * 24 * 60 * 60; // 90 days

  public AuthController(final AuthService authService, final ResponseBuilder responseBuilder) {
    this.authService = authService;
    this.responseBuilder = responseBuilder;
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(HttpServletResponse response) {
    try {
      SignInDto token = authService.signIn();

      Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken().toString());
      accessTokenCookie.setHttpOnly(true);
      accessTokenCookie.setPath("/");
      accessTokenCookie.setMaxAge(ACCESS_TOKEN_EXPIRATION);
      response.addCookie(accessTokenCookie);

      Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken().toString());
      refreshTokenCookie.setHttpOnly(true);
      refreshTokenCookie.setPath("/");
      refreshTokenCookie.setMaxAge(REFRESH_TOKEN_EXPIRATION);
      response.addCookie(refreshTokenCookie);

      return responseBuilder.buildSuccessResponse(token);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to sign in: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    // Cookie から refreshToken を取得
    String refreshToken = null;
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("refreshToken".equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }
    if (refreshToken == null || refreshToken.trim().isEmpty()) {
      return responseBuilder.buildErrorResponse(
          "Refresh token is required.", HttpStatus.BAD_REQUEST);
    }

    try {
      SignInDto token = authService.refreshToken(refreshToken);

      Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken().toString());
      accessTokenCookie.setHttpOnly(true);
      accessTokenCookie.setPath("/");
      accessTokenCookie.setMaxAge(ACCESS_TOKEN_EXPIRATION);
      response.addCookie(accessTokenCookie);

      Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken().toString());
      refreshTokenCookie.setHttpOnly(true);
      refreshTokenCookie.setPath("/");
      refreshTokenCookie.setMaxAge(REFRESH_TOKEN_EXPIRATION);
      response.addCookie(refreshTokenCookie);

      return responseBuilder.buildSuccessResponse(token);
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
