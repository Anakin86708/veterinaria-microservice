package com.ariel.especieService.models;

import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class User {
    private String username;
    private String password;
    private HashSet<Role> roles;

    public User() {
    }

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
