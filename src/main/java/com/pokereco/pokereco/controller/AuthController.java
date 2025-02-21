package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.model.Token;
import com.pokereco.pokereco.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin() {
        Token token = authService.signin();
        return ResponseEntity.ok().body(token);
    }
}
