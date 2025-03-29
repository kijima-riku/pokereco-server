package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.SignInDto;
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

  int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 15 分
//  int REFRESH_TOKEN_EXPIRATION = 90 * 24 * 60 * 60;
  int REFRESH_TOKEN_EXPIRATION = 30 * 60;

  // HTTPS 化ができたので、Cookie は secure にすべき
  private static final boolean LOCAL_HTTPONLY = false;
  private static final boolean LOCAL_SECURE = true; // HTTPS 化後は true に

  public AuthController(final AuthService authService, final ResponseBuilder responseBuilder) {
    this.authService = authService;
    this.responseBuilder = responseBuilder;
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(HttpServletResponse response) {
    System.out.println("signInを受け付けた");
    try {
      SignInDto token = authService.signIn();
      System.out.println("signIn controller=" + token);

      // ResponseCookie を使って SameSite 属性を設定する
      ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getAccessToken().toString())
          .httpOnly(LOCAL_HTTPONLY)
          .secure(LOCAL_SECURE)
          .path("/")
          .maxAge(ACCESS_TOKEN_EXPIRATION)
          .sameSite("None") // 必要に応じて SameSite=None にする方法もあります
          .build();
      response.addHeader("Set-Cookie", accessTokenCookie.toString());

      ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken().toString())
          .httpOnly(LOCAL_HTTPONLY)
          .secure(LOCAL_SECURE)
          .path("/")
          .maxAge(REFRESH_TOKEN_EXPIRATION)
          .sameSite("None")
          .build();
      response.addHeader("Set-Cookie", refreshTokenCookie.toString());

      System.out.println("signIn success: " + refreshTokenCookie);
      return responseBuilder.buildSuccessResponse(token);
    } catch (Exception e) {
      System.out.println("signIn failed" + e);
      return responseBuilder.buildErrorResponse(
          "Failed to sign in: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    // Cookie から refreshToken を取得
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
      SignInDto token = authService.refreshToken(refreshToken);

      ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getAccessToken().toString())
          .httpOnly(LOCAL_HTTPONLY)
          .secure(LOCAL_SECURE)
          .path("/")
          .maxAge(ACCESS_TOKEN_EXPIRATION)
          .sameSite("None")
          .build();
      response.addHeader("Set-Cookie", accessTokenCookie.toString());

      ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken().toString())
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
