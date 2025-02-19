package com.pokereco.pokereco.dto;

public class DecksListDto {
    private Integer id;
    private String name;

    public DecksListDto(final Integer id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
