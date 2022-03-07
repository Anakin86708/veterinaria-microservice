package com.ariel.animalService.controllers;

import com.ariel.animalService.exceptions.ResourceNotFoundException;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.models.Cliente;
import com.ariel.animalService.models.Especie;
import com.ariel.animalService.models.Sexo;
import com.ariel.animalService.services.AnimalService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService mockService;

    @Test
    void getAllAnimaisValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/animais").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getAllAnimaisFromClienteValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/animais/clientes/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }


    @Test
    void getAnimalById() throws Exception {
        MockHttpServletRequestBuilder request = get("/animais/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getAnimalByIdThrowResourceNotFoundException() throws Exception {
        when(mockService.getById(anyLong())).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder request = get("/animais/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void insertAnimalValid() throws Exception {
        Animal animal = spy(new Animal("Mimi", new Date(Instant.now().toEpochMilli()), Sexo.FEMEA, new Especie("gato"), new Cliente("nome", Date.from(Instant.now()), "123", "123", "e@mail.com")));
        when(animal.getId()).thenReturn(0L);
        when(mockService.insertAnimalForClienteId(any(), anyLong())).thenReturn(animal);
        MockHttpServletRequestBuilder request = post("/animais/clientes/0")
                .content(getGson().toJson(animal, Animal.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/animais/0")));
    }

    @Test
    void insertAnimalInvalid() throws Exception {
        Animal animal = spy(new Animal(null, new Date(Instant.now().toEpochMilli()), Sexo.FEMEA, new Especie("gato"), new Cliente("nome", Date.from(Instant.now()), "123", "123", "e@mail.com")));
        when(animal.getId()).thenReturn(0L);
        when(mockService.insertAnimalForClienteId(any(), anyLong())).thenReturn(animal);
        MockHttpServletRequestBuilder request = post("/animais/clientes/0")
                .content(getGson().toJson(animal, Animal.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateAnimalValid() throws Exception {
        Animal animal = spy(new Animal("Mimi", new Date(Instant.now().toEpochMilli()), Sexo.FEMEA, new Especie("gato"), new Cliente("nome", Date.from(Instant.now()), "123", "123", "e@mail.com")));
        when(animal.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/animais/0").content(getGson().toJson(animal, Animal.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void updateAnimalInvalid() throws Exception {
        Animal animal = spy(new Animal(null, new Date(Instant.now().toEpochMilli()), Sexo.FEMEA, new Especie("gato"), new Cliente("nome", Date.from(Instant.now()), "123", "123", "e@mail.com")));
        when(animal.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/animais/0").content(getGson().toJson(animal, Animal.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateAnimalThrowResourceNotFoundException() throws Exception {
        Animal animal = spy(new Animal("Mimi", new Date(Instant.now().toEpochMilli()), Sexo.FEMEA, new Especie("gato"), new Cliente("nome", Date.from(Instant.now()), "123", "123", "e@mail.com")));
        when(animal.getId()).thenReturn(0L);
        when(mockService.updateAnimal(anyLong(), any())).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder request = put("/animais/0").content(getGson().toJson(animal, Animal.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void deleteAnimalByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = delete("/animais/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void deleteAnimalByIdThrowResourceNotFoundException() throws Exception {
        long id = 0L;
        doThrow(new ResourceNotFoundException(id)).when(mockService).deleteAnimal(id);
        MockHttpServletRequestBuilder request = delete("/animais/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    private Gson getGson() {
        return new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    }
}