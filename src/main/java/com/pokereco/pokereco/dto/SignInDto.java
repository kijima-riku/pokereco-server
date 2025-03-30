package com.pokereco.pokereco.dto;

import java.util.UUID;

public record SignInDto(Long userId, UUID accessToken, UUID refreshToken) {}
