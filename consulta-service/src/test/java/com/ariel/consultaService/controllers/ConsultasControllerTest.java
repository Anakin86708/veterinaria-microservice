package com.ariel.consultaService.controllers;

import com.ariel.consultaService.exceptions.ResourceNotFoundException;
import com.ariel.consultaService.models.Animal;
import com.ariel.consultaService.models.Consulta;
import com.ariel.consultaService.models.Veterinario;
import com.ariel.consultaService.services.ConsultaService;
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
@WebMvcTest(controllers = ConsultasController.class)
class ConsultasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService mockService;

    @Test
    void getAllConsultasValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/consultas");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getConsultaByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/consultas/0");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getConsultaByIdThrowResourceNotFoundException() throws Exception {
        when(mockService.getById(anyLong())).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder request = get("/consultas/0");

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void getConsultasByIdAnimalValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/consultas/animal/0");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getConsultasByIdVeterinarioValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/consultas/veterinario/0");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void insertConsultaValid() throws Exception {
        Consulta consulta = spy(new Consulta(Date.from(Instant.now()), "com", new Animal(), new Veterinario(), false));
        when(consulta.getId()).thenReturn(0L);
        when(mockService.insertConsulta(any())).thenReturn(consulta);
        MockHttpServletRequestBuilder request = post("/consultas")
                .content(getGson().toJson(consulta, Consulta.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/consultas/0")));
    }

    @Test
    void insertConsultaInvalid() throws Exception {
        Consulta consulta = spy(new Consulta(Date.from(Instant.now()), null, null, new Veterinario(), false));
        when(consulta.getId()).thenReturn(0L);
        when(mockService.insertConsulta(any())).thenReturn(consulta);
        MockHttpServletRequestBuilder request = post("/consultas")
                .content(getGson().toJson(consulta, Consulta.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }


    @Test
    void updateConsultaByIdValid() throws Exception {
        Consulta consulta = spy(new Consulta(Date.from(Instant.now()), "com", new Animal(), new Veterinario(), false));
        when(consulta.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/consultas/0")
                .content(getGson().toJson(consulta, Consulta.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void updateConsultaByIdInvalid() throws Exception {
        Consulta consulta = spy(new Consulta(Date.from(Instant.now()), null, null, new Veterinario(), false));
        when(consulta.getId()).thenReturn(0L);
        MockHttpServletRequestBuilder request = put("/consultas/0")
                .content(getGson().toJson(consulta, Consulta.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void deleteConsultaByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = delete("/consultas/0");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void deleteConsultaByIdThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException(0L)).when(mockService).deleteConsulta(0L);
        MockHttpServletRequestBuilder request = delete("/consultas/0");

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    private Gson getGson() {
        return new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    }
}