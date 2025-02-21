package com.pokereco.pokereco.dto;

import java.util.UUID;

public class SignInDto {
    public Long userId;
    public UUID accessToken;
    public UUID refreshToken;

    public SignInDto(final Long userId, final UUID accessToken, final UUID refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public UUID getAccessToken() {return accessToken;}

    public UUID getRefreshToken() {return refreshToken;}
}
