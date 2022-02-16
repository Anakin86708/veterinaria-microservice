package com.ariel.consultaService.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Consulta {
    @Id
    @GeneratedValue
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataAgendada;
    private String comentarios;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @OneToOne
    @JoinColumn(name = "veterinario_id")
    private Veterinario veterinario;

    private boolean finalizado;

    public Consulta(Date dataAgendada, String comentarios, Animal animal, Veterinario veterinario, boolean finalizado) {
        this.dataAgendada = dataAgendada;
        this.comentarios = comentarios;
        this.animal = animal;
        this.veterinario = veterinario;
        this.finalizado = finalizado;
    }

    public Consulta() {
    }

    public Date getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(Date dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Long getId() {
        return id;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
