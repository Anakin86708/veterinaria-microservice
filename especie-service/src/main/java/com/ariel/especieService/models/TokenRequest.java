package com.ariel.especieService.models;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
public class TokenRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}