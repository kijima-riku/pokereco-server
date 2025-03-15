package com.pokereco.pokereco.dto;

public class ResultPostResponseDto {
    private Long id;
    private int myDeck;
    private int opponentDeck;
    private boolean isFirst;
    private short turnCount;
    private short outcome;

    public ResultPostResponseDto(
            final Long userId,
            final int myDeck,
            final int opponentDeck,
            final boolean isFirst,
            final short turnCount,
            final short outcome
    ){
        this.id = userId;
        this.myDeck = myDeck;
        this.opponentDeck = opponentDeck;
        this.isFirst = isFirst;
        this.turnCount = turnCount;
        this.outcome = outcome;
    }

    public Long getId() {
        return id;
    }

    public int getMyDeck() {
        return myDeck;
    }

    public int getOpponentDeck() {
        return opponentDeck;
    }

    public boolean isIsFirst() {
        return isFirst;
    }

    public short getTurnCount() {
        return turnCount;
    }

    public short getOutcome() {
        return outcome;
    }
}
