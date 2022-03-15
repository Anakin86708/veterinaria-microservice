package com.ariel.veterinariaauth.payloads.requests;

import java.util.Set;

public class SignupRequest {
    private final String username;
    private final String email;
    private final String password;
    private final Set<String> roles;

    public SignupRequest(String username, String email, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
