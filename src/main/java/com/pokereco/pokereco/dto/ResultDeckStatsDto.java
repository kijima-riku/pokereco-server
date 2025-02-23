package com.pokereco.pokereco.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDeckStatsDto {
    private Integer deckId;
    private Long totalMatches;
    private Double winRate;
    private Integer bestDeckId;
    private Double bestDeckWinRate;

    public ResultDeckStatsDto(Integer deckId, Long totalMatches, Double winRate) {
        this.deckId = deckId;
        this.totalMatches = totalMatches;
        this.winRate = winRate;
    }

    public ResultDeckStatsDto(Long totalMatches, Double winRate, Integer bestDeckId, Double bestDeckWinRate){
        this.totalMatches = totalMatches;
        this.winRate = winRate;
        this.bestDeckId = bestDeckId;
        this.bestDeckWinRate = bestDeckWinRate;
    }

    public Integer getDeckId() { return deckId; }
    public Long getTotalMatches() { return totalMatches; }
    public Double getWinRate() { return winRate; }
    public Integer getBestDeckId() {return bestDeckId;}
    public Double getBestDeckWinRate() {return bestDeckWinRate;}
}
