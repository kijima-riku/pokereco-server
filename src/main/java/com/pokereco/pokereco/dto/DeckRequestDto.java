package com.pokereco.pokereco.dto;

import jakarta.annotation.Nullable;

public class DeckRequestDto {
    private String mainName;
    @Nullable
    private String subName;

    public DeckRequestDto(final String mainName, @Nullable final String subName) {
        this.mainName = mainName;
        this.subName = subName;
    }

    public String getMainName() {return mainName;}
    public String getSubName() {return subName;}
}
