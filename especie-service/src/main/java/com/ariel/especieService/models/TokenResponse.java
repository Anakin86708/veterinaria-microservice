package com.ariel.especieService.models;

import org.springframework.stereotype.Component;

@Component
public class TokenResponse {
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
