package com.ariel.userservice.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
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

    public void setName(ERole name) {
        this.name = name;
    }
}
