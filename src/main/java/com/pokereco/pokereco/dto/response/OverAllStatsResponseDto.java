package com.pokereco.pokereco.dto.response;

public record OverAllStatsResponseDto(
    Long totalMatches,
    Double winRate,
    Integer bestDeckId,
    Double bestDeckWinRate,
    long bestDeckMatches) {
  public OverAllStatsResponseDto {
    winRate = winRate * 100;
    bestDeckWinRate = bestDeckWinRate * 100;
  }
}
