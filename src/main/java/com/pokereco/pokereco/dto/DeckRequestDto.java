package com.pokereco.pokereco.dto;

import jakarta.annotation.Nullable;

public record DeckRequestDto(String mainName, @Nullable String subName) {}
