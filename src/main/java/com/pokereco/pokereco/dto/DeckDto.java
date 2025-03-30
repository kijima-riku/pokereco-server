package com.pokereco.pokereco.dto;

import jakarta.annotation.Nullable;

public record DeckDto(Integer id, String mainName, @Nullable String subName) {}
