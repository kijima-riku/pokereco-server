package com.pokereco.pokereco.dto.request;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record DeckStatsRequestDto(
    @Nullable Boolean isFirst,
    @Nullable LocalDateTime startDate,
    @Nullable LocalDateTime endDate) {}
