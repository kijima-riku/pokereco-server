package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.SignInDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;
  private final ResponseBuilder responseBuilder;

  public AuthController(final AuthService authService, final ResponseBuilder responseBuilder) {
    this.authService = authService;
    this.responseBuilder = responseBuilder;
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signIn() {
    SignInDto token = authService.signIn();
    return responseBuilder.buildSuccessResponse(token);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
    if (refreshToken.trim().isEmpty()) {
      return responseBuilder.buildErrorResponse(
          "refresh token is required.", HttpStatus.BAD_REQUEST);
    }
    if (refreshToken.startsWith("Bearer")) {
      refreshToken = refreshToken.substring(7);
    }
    try {
      SignInDto token = authService.refreshToken(refreshToken);
      return responseBuilder.buildSuccessResponse(token);
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
