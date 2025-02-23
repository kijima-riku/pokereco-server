package com.pokereco.pokereco.Security;

public class CustomUserPrincipal {
    private final Long userId;

    public CustomUserPrincipal(final Long userid) {
        this.userId = userid;
    }
    public Long getUserId() {return userId;}
}
