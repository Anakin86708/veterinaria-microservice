package com.ariel.especieService.models;

import org.springframework.stereotype.Component;

@Component
public class Role {

    private Long id;
    private ERole name;

    public Role() {
    }

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }
}
