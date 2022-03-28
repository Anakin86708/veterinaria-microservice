package com.ariel.animalService.controllers;

import com.ariel.animalService.AnimalServiceApplication;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.models.Cliente;
import com.ariel.animalService.models.Especie;
import com.ariel.animalService.models.Sexo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(value = {"file:///home/atadeu/Documents/trilha_capacitacao/projetoVeterinariaMicroservices/animal-service/src/test/java/com/ariel/animalService/data/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"file:///home/atadeu/Documents/trilha_capacitacao/projetoVeterinariaMicroservices/animal-service/src/test/java/com/ariel/animalService/data/after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(classes = {AnimalServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
class AnimalControllerITTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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

    @Test
    void insertAnimal() {
        String url = buildUrl("/animais/clientes/1");
        Cliente clientePertencente = new Cliente(1L, "Cliente1", Date.from(Instant.now()), "a", "123", "e@mail.br");
        Especie especie = new Especie(3L, "javanes");
        Animal newAnimal = new Animal("Max", Date.from(Instant.now()), Sexo.MACHO, especie, clientePertencente);

        ResponseEntity<Animal> entity = restTemplate.postForEntity(url, newAnimal, Animal.class);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    void updateAnimal() {
        String url = buildUrl("/animais/1");
        Cliente clientePertencente = new Cliente(1L, "Cliente0", Date.from(Instant.now()), "a", "123", "e@mail.br");
        Especie especie = new Especie(3L, "javanes");
        Animal newAnimal = new Animal(1L, "Bobcat", Date.from(Instant.now()), Sexo.MACHO, especie, clientePertencente);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        String body = getGson().toJson(newAnimal);

        restTemplate.put(url, newAnimal);
        HttpEntity<String> request = new HttpEntity<>(body, header);
        ResponseEntity<Animal> exchange = restTemplate.exchange(url, HttpMethod.PUT, request, Animal.class);

        assertTrue(exchange.getStatusCode().is2xxSuccessful());
    }

    @Test
    void deleteAnimalById() {
        long id = 1L;
        String url = buildUrl("/animais/" + id);

        ResponseEntity<Void> exchange = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertTrue(exchange.getStatusCode().is2xxSuccessful());
    }

    private String buildUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    private Gson getGson() {
        return new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    }
}