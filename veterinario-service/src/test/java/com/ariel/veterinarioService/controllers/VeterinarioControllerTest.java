package com.ariel.veterinarioService.controllers;

import com.ariel.veterinarioService.exceptions.ActiveForeignKeyException;
import com.ariel.veterinarioService.exceptions.ResourceNotFoundException;
import com.ariel.veterinarioService.models.Veterinario;
import com.ariel.veterinarioService.services.VeterinarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VeterinarioController.class)
class VeterinarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VeterinarioService mockService;

    @Test
    public void getAllVeterinariosValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/veterinarios").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void getVeterinarioByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/veterinarios/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void getVeterinarioByIdThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException(0L)).when(mockService).getById(0L);
        MockHttpServletRequestBuilder request = get("/veterinarios/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    public void insertVeterinarioValid() throws Exception {
        Veterinario veterinario = spy(new Veterinario("Vet", "Rua 1", "123"));
        when(veterinario.getId()).thenReturn(0L);
        when(mockService.insertVeterinario(any(Veterinario.class))).thenReturn(veterinario);
        MockHttpServletRequestBuilder request = post("/veterinarios")
                .content(new Gson().toJson(veterinario, Veterinario.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.
                andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void insertVeterinarioInvalid() throws Exception {
        Veterinario veterinario = spy(new Veterinario(null, null, null));
        when(veterinario.getId()).thenReturn(0L);
        when(mockService.insertVeterinario(any(Veterinario.class))).thenReturn(veterinario);
        MockHttpServletRequestBuilder request = post("/veterinarios")
                .content(new Gson().toJson(veterinario, Veterinario.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void updateVeterinarioValid() throws Exception {
        Veterinario veterinario = spy(new Veterinario("Vet", "Rua 1", "123"));
        when(veterinario.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/veterinarios/0")
                .content(new Gson().toJson(veterinario, Veterinario.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void updateVeterinarioInvalid() throws Exception {
        Veterinario veterinario = spy(new Veterinario(null, "Rua 1", "123"));
        when(veterinario.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/veterinarios/0")
                .content(new Gson().toJson(veterinario, Veterinario.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteVeterinarioByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = delete("/veterinarios/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void deleteVeterinarioByIdThrowActiveForeignKeyException() throws Exception {
        doThrow(new ActiveForeignKeyException(0L)).when(mockService).deleteVeterinarioById(0L);
        MockHttpServletRequestBuilder request = delete("/veterinarios/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ActiveForeignKeyException));
    }

    @Test
    public void deleteVeterinarioByIdThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException(0L)).when(mockService).deleteVeterinarioById(0L);
        MockHttpServletRequestBuilder request = delete("/veterinarios/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }
}