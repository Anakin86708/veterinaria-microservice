package com.ariel.especieService.controllers;

import com.ariel.especieService.models.Especie;
import com.ariel.especieService.services.EspecieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EspecieController.class)
class EspecieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EspecieService mockService;

    @Test
    public void getAllEspeciesValid() throws Exception {
        RequestBuilder request = get("/especies").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void getEspecieByIdValid() throws Exception {
        RequestBuilder request = get("/especies/1").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void getEspecieByNameValid() throws Exception {
        RequestBuilder request = get("/especies/byName")
                .param("name", "Teste").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void insertEspecieValid() throws Exception {
        Especie especie = spy(new Especie("gato"));
        Mockito.when(especie.getId()).thenReturn(0L);
        Mockito.when(mockService.insertEspecie(Mockito.any(Especie.class))).thenReturn(especie);
        MockHttpServletRequestBuilder request = post("/especies")
                .content(new Gson().toJson(especie, Especie.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void insertEspecieInvalid() throws Exception {
        Especie especie = spy(new Especie(null));
        Mockito.when(especie.getId()).thenReturn(0L);
        Mockito.when(mockService.insertEspecie(Mockito.any(Especie.class))).thenReturn(especie);
        MockHttpServletRequestBuilder request = post("/especies")
                .content(new Gson().toJson(especie, Especie.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void updateEspecieValid() throws Exception {
        long id = 0L;
        Especie especie = Mockito.spy(new Especie("gato"));
        Mockito.when(especie.getId()).thenReturn(id);
        RequestBuilder request = put("/especies/" + id)
                .content(new Gson().toJson(especie, Especie.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void deleteEspecieByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = delete("/especies/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

}