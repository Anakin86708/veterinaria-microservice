package com.ariel.veterinariaauth.payloads.requests;

import javax.validation.constraints.NotBlank;

public class SignInRequest {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    public SignInRequest(String username, String password) {
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
