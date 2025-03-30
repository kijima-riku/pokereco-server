package com.pokereco.pokereco.dto.request;

public record ResultPostRequestDto(
    int myDeck, int opponentDeck, boolean isFirst, short turnCount, short outcome) {}
