package com.ariel.consultaService.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Consulta {
    @Id
    @GeneratedValue
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private Date dataAgendada;
    @NotBlank
    private String comentarios;

    @OneToOne(optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    @NotNull
    private Animal animal;

    @OneToOne(optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    @NotNull
    private Veterinario veterinario;

    @NotNull
    private boolean finalizado;

    public Consulta(long id, Date dataAgendada, String comentarios, Animal animal, Veterinario veterinario, boolean finalizado) {
        this.id = id;
        this.dataAgendada = dataAgendada;
        this.comentarios = comentarios;
        this.animal = animal;
        this.veterinario = veterinario;
        this.finalizado = finalizado;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;
        return finalizado == consulta.finalizado && Objects.equals(id, consulta.id) && Objects.equals(dataAgendada, consulta.dataAgendada) && Objects.equals(comentarios, consulta.comentarios) && Objects.equals(animal, consulta.animal) && Objects.equals(veterinario, consulta.veterinario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataAgendada, comentarios, animal, veterinario, finalizado);
    }
}
