package com.pokereco.pokereco.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_decks")
public class FavoriteDeck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "deck_id", nullable = false)
    private Integer deckId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true)
    private LocalDateTime updatedAt;

    public FavoriteDeck() {}

    public FavoriteDeck(Long userId, Integer deckId) {
        this.userId = userId;
        this.deckId = deckId;
    }

    public Integer getId() {return id;}
    public Long getUserId() {return userId;}
    public Integer getDeckId() {return deckId;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }
}
