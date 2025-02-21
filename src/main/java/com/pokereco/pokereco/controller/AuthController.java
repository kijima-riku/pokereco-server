package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.SignInDto;
import com.pokereco.pokereco.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn() {
        SignInDto token = authService.signIn();
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken){
        System.out.println("refreshToken"+refreshToken);
        if(refreshToken.startsWith("Bearer")){
            refreshToken = refreshToken.substring(7);
        }
        SignInDto token = authService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(token);
    }
}
