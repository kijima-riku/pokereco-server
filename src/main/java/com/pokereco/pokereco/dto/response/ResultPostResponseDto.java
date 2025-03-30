package com.pokereco.pokereco.dto.response;

public record ResultPostResponseDto(
    Long id, int myDeck, int opponentDeck, boolean isFirst, short turnCount, short outcome) {}
