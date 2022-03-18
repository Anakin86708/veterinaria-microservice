package com.ariel.animalService.controllers;

import com.ariel.animalService.AnimalServiceApplication;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.models.Cliente;
import com.ariel.animalService.models.Especie;
import com.ariel.animalService.models.Sexo;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {AnimalServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
class AnimalControllerITTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql(value = {"file:///home/atadeu/Documents/trilha_capacitacao/projetoVeterinariaMicroservices/animal-service/src/test/java/com/ariel/animalService/data/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"file:///home/atadeu/Documents/trilha_capacitacao/projetoVeterinariaMicroservices/animal-service/src/test/java/com/ariel/animalService/data/after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getAllAnimais() {
        String url = buildUrl("/animais");

        ResponseEntity<List> entity = restTemplate.getForEntity(url, List.class);
        List object = entity.getBody();
        assertThat(entity.getStatusCode().is2xxSuccessful());
        assertThat(object.stream().map(o -> assertInstanceOf(Animal.class, o)));
    }

    @Test
    void getAllAnimaisFromCliente() {
        String url = buildUrl("/animais/clientes/3");

        ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List object = (List) entity.getBody();
        assertThat(object.stream().map(o -> assertInstanceOf(Animal.class, o)));
    }

    @Test
    void getAnimalById() {
        String url = buildUrl("/animais/10");

        Animal object = restTemplate.getForObject(url, Animal.class);

        assertThat(Objects.nonNull(object));
        assertInstanceOf(Animal.class, object);
    }

    @Sql(scripts = {"file:///home/atadeu/Documents/trilha_capacitacao/projetoVeterinariaMicroservices/animal-service/src/test/java/com/ariel/animalService/data/after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void insertAnimal() {
        String url = buildUrl("/animais/clientes/1");
        Cliente clientePertencente = new Cliente(0L, "Cliente0", Date.from(Instant.now()), "a", "123", "e@mail.br");
        Especie especie = new Especie(0L, "não definido");
        Animal newAnimal = new Animal(0L, "Max", Date.from(Instant.now()), Sexo.MACHO, especie, clientePertencente);

        ResponseEntity<Animal> entity = restTemplate.postForEntity(url, newAnimal, Animal.class);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        Animal object = entity.getBody();
        assertInstanceOf(Animal.class, object);
    }

    @Test
    void updateAnimal() {
        String url = buildUrl("/animais/1");
        Cliente clientePertencente = new Cliente(1L, "Cliente0", Date.from(Instant.now()), "a", "123", "e@mail.br");
        Especie especie = new Especie(1L, "não definido");
        Animal newAnimal = new Animal(1L, "Max", Date.from(Instant.now()), Sexo.MACHO, especie, clientePertencente);

        restTemplate.put(url, newAnimal);
        String body = new GsonBuilder().setDateFormat("yyyy-mm-dd").create().toJson(newAnimal);
        HttpEntity<String> request = new HttpEntity<>(body);
        ResponseEntity<Animal> exchange = restTemplate.exchange(url, HttpMethod.PUT, request, Animal.class);

        assertTrue(exchange.getStatusCode().is2xxSuccessful());
    }

    @Test
    void deleteAnimalById() {
    }

    private String buildUrl(String uri) {
        return "http://localhost:" + port + uri;
    }
}