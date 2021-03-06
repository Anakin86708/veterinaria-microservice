package com.ariel.animalService.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
public class Animal {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String nome;

    @PastOrPresent
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataNascimento;

    @NotNull
    @Enumerated
    private Sexo sexo;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private Especie especie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_pertencente_id", nullable = false)
    private Cliente clientePertencente;

    public Animal() {

    }

    public Animal(String nome, @NotBlank @Size(max = 30) Date dataNascimento, Sexo sexo, Especie especie, Cliente clientePertencente) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.especie = especie;
        this.clientePertencente = clientePertencente;
    }

    public Animal(Long id, String nome, @NotBlank @Size(max = 30) Date dataNascimento, Sexo sexo, Especie especie, Cliente clientePertencente) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.especie = especie;
        this.clientePertencente = clientePertencente;
    }

    public Cliente getClientePertencente() {
        return clientePertencente;
    }

    public void setClientePertencente(Cliente clientePertencente) {
        this.clientePertencente = clientePertencente;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id) && Objects.equals(nome, animal.nome) && Objects.equals(dataNascimento, animal.dataNascimento) && sexo == animal.sexo && Objects.equals(especie, animal.especie) && Objects.equals(clientePertencente, animal.clientePertencente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataNascimento, sexo, especie, clientePertencente);
    }
}
