package com.pokereco.pokereco.dto.request;

import jakarta.annotation.Nullable;

public record DeckRequestDto(String mainName, @Nullable String subName) {}
