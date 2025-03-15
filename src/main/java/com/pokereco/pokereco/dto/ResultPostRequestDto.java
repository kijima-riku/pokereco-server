package com.pokereco.pokereco.dto;

public class ResultPostRequestDto {
    private int myDeck;
    private int opponentDeck;
    private boolean isFirst;
    private short turnCount;
    private short outcome;


    public int getMyDeck() {
        return myDeck;
    }

    public void setMyDeck(int myDeck) {
        this.myDeck = myDeck;
    }

    public int getOpponentDeck() {
        return opponentDeck;
    }

    public void setOpponentDeck(int opponentDeck) {
        this.opponentDeck = opponentDeck;
    }

    public boolean isIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public short getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(short turnCount) {
        this.turnCount = turnCount;
    }

    public short getOutcome() {
        return outcome;
    }

    public void setOutcome(short outcome) {
        this.outcome = outcome;
    }
}
