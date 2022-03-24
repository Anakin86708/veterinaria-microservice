package com.ariel.especieService.models;

import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class User {
    private final String username;
    private final String password;
    private final HashSet<Role> roles;

    public User(String username, String password, HashSet<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public HashSet<Role> getRoles() {
        return new HashSet<>(roles);
    }
}
