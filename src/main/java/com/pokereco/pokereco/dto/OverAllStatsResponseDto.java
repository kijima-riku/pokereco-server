package com.pokereco.pokereco.dto;

public record OverAllStatsResponseDto(
    Long totalMatches, Double winRate, Integer bestDeckId, Double bestDeckWinRate) {}
