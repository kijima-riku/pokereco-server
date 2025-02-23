package com.pokereco.pokereco.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_decks")
public class UserDeck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "deck_id", nullable = false)
    private Integer deckId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UserDeck() {}

    public UserDeck(Long userId, Integer deckId) {
        this.userId = userId;
        this.deckId = deckId;
    }

    public Integer getId() {return id;}
    public Long getUserId() {return userId;}
    public Integer getDeckId() {return deckId;}
}
