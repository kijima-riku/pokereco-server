package com.pokereco.pokereco.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private UUID accessToken;

    @Column(nullable = false, unique = true)
    private UUID refreshToken;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Token() {}

    public Token(User user, UUID accessToken, UUID refreshToken){
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public UUID getAccessToken() { return accessToken; }
    public UUID getRefreshToken() { return refreshToken; }
    public LocalDateTime getCreatedAt(){return createdAt;}
}
