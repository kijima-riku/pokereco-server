package com.pokereco.pokereco.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID userKey;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(UUID userKey) {
        this.userKey = userKey;
    }

    public Long getId() { return id; }
    public UUID getUserKey() { return userKey; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Token> getTokens() { return tokens; }
}
