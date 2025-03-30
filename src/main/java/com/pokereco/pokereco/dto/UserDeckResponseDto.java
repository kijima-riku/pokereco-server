package com.pokereco.pokereco.dto;

import java.time.LocalDateTime;

public record UserDeckResponseDto(Integer deckId, LocalDateTime createdAt) {}
