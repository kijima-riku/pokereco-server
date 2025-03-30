package com.pokereco.pokereco.dto;

import com.pokereco.pokereco.model.Deck;
import java.time.LocalDateTime;

public record MatchDto(
    long id,
    long userId,
    Deck myDeck,
    Deck opponentDeck,
    boolean isFirst,
    int turnCount,
    short outcome,
    LocalDateTime createdAt) {}
