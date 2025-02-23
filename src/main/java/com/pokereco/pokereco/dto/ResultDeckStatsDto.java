package com.pokereco.pokereco.dto;

public class ResultDeckStatsDto {
    private Integer deckId;
    private Long totalMatches;
    private Double winRate;

    public ResultDeckStatsDto(Integer deckId, Long totalMatches, Double winRate) {
        this.deckId = deckId;
        this.totalMatches = totalMatches;
        this.winRate = winRate;
    }

    public Integer getDeckId() { return deckId; }
    public Long getTotalMatches() { return totalMatches; }
    public Double getWinRate() { return winRate; }
}
