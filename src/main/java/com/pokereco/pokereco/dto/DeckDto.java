package com.pokereco.pokereco.dto;

public class DeckDto {
    private Integer id;
    private String mainName;
    private String subName;

    public DeckDto(final Integer id, final String mainName, final String subName) {
        this.id = id;
        this.mainName = mainName;
        this.subName = subName;
    }

    public Integer getId() {
        return id;
    }
    public String getMainName() {return mainName;}
    public String getSubName() {return subName;}
}
