package com.pokereco.pokereco.dto.response;

import java.time.LocalDateTime;

public record UserDeckResponseDto(Integer deckId, LocalDateTime createdAt) {}
