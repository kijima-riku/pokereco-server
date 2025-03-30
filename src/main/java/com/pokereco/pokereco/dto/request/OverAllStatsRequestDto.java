package com.pokereco.pokereco.dto.request;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record OverAllStatsRequestDto(
    @Nullable Integer limit,
    @Nullable Integer page,
    @Nullable Boolean isFirst,
    @Nullable LocalDateTime startDate,
    @Nullable LocalDateTime endDate) {}
