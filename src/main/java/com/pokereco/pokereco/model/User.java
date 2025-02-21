package com.pokereco.pokereco.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userKey;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String userKey) {
        this.userKey = userKey;
    }

    public Long getId() {return id;}

    public String getUserKey() {return userKey;}

    public LocalDateTime getCreatedAt() {return getCreatedAt();}
}
