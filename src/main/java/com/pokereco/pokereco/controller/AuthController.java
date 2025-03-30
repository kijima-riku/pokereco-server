package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.response.AuthResponseDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;
  private final ResponseBuilder responseBuilder;

  static final int ACCESS_TOKEN_EXPIRATION = 15 * 60;
  // static final  int REFRESH_TOKEN_EXPIRATION = 90 * 24 * 60 * 60;
  static final int REFRESH_TOKEN_EXPIRATION = 30 * 60;

  private static final boolean LOCAL_HTTPONLY = false;
  private static final boolean LOCAL_SECURE = true;

  AuthController(final AuthService authService, final ResponseBuilder responseBuilder) {
    this.authService = authService;
    this.responseBuilder = responseBuilder;
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(final HttpServletResponse response) {
    try {
      final AuthResponseDto token = authService.signIn();

      final ResponseCookie accessTokenCookie =
          ResponseCookie.from("accessToken", token.accessToken().toString())
              .httpOnly(LOCAL_HTTPONLY)
              .secure(LOCAL_SECURE)
              .path("/")
              .maxAge(ACCESS_TOKEN_EXPIRATION)
              .sameSite("None")
              .build();
      response.addHeader("Set-Cookie", accessTokenCookie.toString());

      final ResponseCookie refreshTokenCookie =
          ResponseCookie.from("refreshToken", token.refreshToken().toString())
              .httpOnly(LOCAL_HTTPONLY)
              .secure(LOCAL_SECURE)
              .path("/")
              .maxAge(REFRESH_TOKEN_EXPIRATION)
              .sameSite("None")
              .build();
      response.addHeader("Set-Cookie", refreshTokenCookie.toString());

      return responseBuilder.buildSuccessResponse(token);
    } catch (final Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to sign in: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(
      final HttpServletRequest request, final HttpServletResponse response) {
    String refreshToken = null;
    if (request.getCookies() != null) {
      for (var cookie : request.getCookies()) {
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
      AuthResponseDto token = authService.refreshToken(refreshToken);

      ResponseCookie accessTokenCookie =
          ResponseCookie.from("accessToken", token.accessToken().toString())
              .httpOnly(LOCAL_HTTPONLY)
              .secure(LOCAL_SECURE)
              .path("/")
              .maxAge(ACCESS_TOKEN_EXPIRATION)
              .sameSite("None")
              .build();
      response.addHeader("Set-Cookie", accessTokenCookie.toString());

      ResponseCookie refreshTokenCookie =
          ResponseCookie.from("refreshToken", token.refreshToken().toString())
              .httpOnly(LOCAL_HTTPONLY)
              .secure(LOCAL_SECURE)
              .path("/")
              .maxAge(REFRESH_TOKEN_EXPIRATION)
              .sameSite("None")
              .build();
      response.addHeader("Set-Cookie", refreshTokenCookie.toString());

      return responseBuilder.buildSuccessResponse(token);
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
