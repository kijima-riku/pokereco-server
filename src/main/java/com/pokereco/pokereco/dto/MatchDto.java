package com.pokereco.pokereco.dto;

import com.pokereco.pokereco.model.Deck;
import java.time.LocalDateTime;

public class MatchDto {
  public long id;
  public long userId;
  public Deck myDeck;
  public Deck opponentDeck;
  public boolean isFirst;
  public int turnCount;
  public short outcome;
  public LocalDateTime createdAt;

  public MatchDto(
      final long id,
      final long userId,
      final Deck myDeck,
      final Deck opponentDeck,
      final boolean isFirst,
      final int turnCount,
      final short outcome,
      final LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.myDeck = myDeck;
    this.opponentDeck = opponentDeck;
    this.isFirst = isFirst;
    this.turnCount = turnCount;
    this.outcome = outcome;
    this.createdAt = createdAt;
  }

  public long getId() {
    return id;
  }

  public long getUserId() {
    return userId;
  }

  public Deck getMyDeck() {
    return myDeck;
  }

  public Deck getOpponentDeck() {
    return opponentDeck;
  }

  public boolean isFirst() {
    return isFirst;
  }

  public int getTurnCount() {
    return turnCount;
  }

  public short getOutcome() {
    return outcome;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
