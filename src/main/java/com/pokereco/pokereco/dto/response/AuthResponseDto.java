package com.pokereco.pokereco.dto.response;

import java.util.UUID;

public record AuthResponseDto(Long userId, UUID accessToken, UUID refreshToken) {}
