package com.pokereco.pokereco.dto.request;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record MatchRequestDto(
    @Nullable Integer limit,
    @Nullable Integer page,
    @Nullable Integer deckId,
    @Nullable Integer opponentDeckId,
    @Nullable Boolean isFirst,
    @Nullable LocalDateTime startDate,
    @Nullable LocalDateTime endDate) {}
