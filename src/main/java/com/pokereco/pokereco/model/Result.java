package com.pokereco.pokereco.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "my_deck", nullable = false)
    private Deck myDeck;

    @ManyToOne
    @JoinColumn(name = "opponent_deck", nullable = false)
    private Deck opponentDeck;

    @Column(name = "is_first", nullable = false)
    private boolean isFirst;

    @Column(name = "turn_count", nullable = false)
    private short turnCount;

    @Column(name = "outcome", nullable = false)
    private short outcome;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Result() {}

    public Result(User user, Deck myDeck, Deck opponentDeck, boolean isFirst, short turnCount, short outcome) {
        this.user = user;
        this.myDeck = myDeck;
        this.opponentDeck = opponentDeck;
        this.isFirst = isFirst;
        this.turnCount = turnCount;
        this.outcome = outcome;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Deck getMyDeck() { return myDeck; }
    public Deck getOpponentDeck() { return opponentDeck; }
    public boolean isFirst() { return isFirst; }
    public short getTurnCount() { return turnCount; }
    public short getOutcome() { return outcome; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
