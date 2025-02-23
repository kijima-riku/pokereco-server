package com.pokereco.pokereco.dto;

import jakarta.annotation.Nullable;

public class ResultRequestDto {
    @Nullable
    private Integer limit;
    @Nullable
    private Integer page;
    @Nullable
    private Integer deckId;
    @Nullable
    private Integer opponentDeckId;
    @Nullable
    private Boolean isFirst;
    @Nullable
    private String startDate;
    @Nullable
    private String endDate;
    @Nullable
    private Short outcome;

    public ResultRequestDto() {}

    @Nullable
    public Integer getLimit() { return limit; }
    @Nullable
    public Integer getPage() { return page; }
    @Nullable
    public Integer getDeckId() { return deckId; }
    @Nullable
    public Integer getOpponentDeckId() { return opponentDeckId; }
    @Nullable
    public Boolean getIsFirst() { return isFirst; }
    @Nullable
    public String getStartDate() { return startDate; }
    @Nullable
    public String getEndDate() { return endDate; }
    @Nullable
    public Short getOutcome() {return outcome;}

    public void setLimit(Integer limit) { this.limit = limit; }
    public void setPage(Integer page) { this.page = page; }
    public void setDeckId(Integer deckId) { this.deckId = deckId; }
    public void setOpponentDeckId(Integer opponentDeckId) { this.opponentDeckId = opponentDeckId; }
    public void setIsFirst(Boolean isFirst) { this.isFirst = isFirst; }
    public void setOutcome(Short outcome) { this.outcome = outcome; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
