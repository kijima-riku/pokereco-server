package com.pokereco.pokereco.dto;

import jakarta.annotation.Nullable;

public record OverAllStatsRequestDto(
    @Nullable Integer limit,
    @Nullable Integer page,
    @Nullable Integer deckId,
    @Nullable Integer opponentDeckId,
    @Nullable Boolean isFirst,
    @Nullable String startDate,
    @Nullable String endDate,
    @Nullable Short outcome) {}
