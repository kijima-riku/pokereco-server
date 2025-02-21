package com.pokereco.pokereco.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private String accessToken;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Token() {}

    public Token(User user, String accessToken, String refreshToken){
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
