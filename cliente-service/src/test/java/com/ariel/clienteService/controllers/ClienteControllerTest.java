package com.ariel.clienteService.controllers;

import com.ariel.clienteService.exceptions.ResourceNotFoundException;
import com.ariel.clienteService.models.Cliente;
import com.ariel.clienteService.services.ClienteService;
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
@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService mockService;

    @Test
    void getAllClientesValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/clientes").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getClienteByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = get("/clientes/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void getClienteByIdThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException(0L)).when(mockService).getById(0L);
        MockHttpServletRequestBuilder request = get("/clientes/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void insertClienteValid() throws Exception {
        Cliente cliente = spy(new Cliente("Teste", new Date(Instant.now().toEpochMilli() - 1000), "Rua 1", "123", "teste@mail.com"));
        when(cliente.getId()).thenReturn(0L);
        when(mockService.insertCliente(any(Cliente.class))).thenReturn(cliente);

        MockHttpServletRequestBuilder request = post("/clientes")
                .content(getGson().toJson(cliente, Cliente.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/clientes/0")));
    }

    @Test
    void insertClienteInvalid() throws Exception {
        Cliente cliente = spy(new Cliente(null, new Date(Instant.now().toEpochMilli() - 1000), "Rua 1", "123", "teste@mail.com"));
        when(cliente.getId()).thenReturn(0L);
        when(mockService.insertCliente(any(Cliente.class))).thenReturn(cliente);

        MockHttpServletRequestBuilder request = post("/clientes")
                .content(getGson().toJson(cliente, Cliente.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateClienteValid() throws Exception {
        long id = 0L;
        Cliente cliente = spy(new Cliente("Teste", new Date(Instant.now().toEpochMilli() - 1000), "Rua 1", "123", "teste@mail.com"));
        when(cliente.getId()).thenReturn(id);
        MockHttpServletRequestBuilder request = put("/clientes/" + id)
                .content(getGson().toJson(cliente, Cliente.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void updateClienteInalid() throws Exception {
        long id = 0L;
        Cliente cliente = spy(new Cliente(null, new Date(Instant.now().toEpochMilli() - 1000), "Rua 1", "123", "teste@mail.com"));
        when(cliente.getId()).thenReturn(id);
        MockHttpServletRequestBuilder request = put("/clientes/" + id)
                .content(getGson().toJson(cliente, Cliente.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateClienteThrowResourceNotFoundException() throws Exception {
        long id = 0L;
        Cliente cliente = spy(new Cliente("Teste", new Date(Instant.now().toEpochMilli() - 1000), "Rua 1", "123", "teste@mail.com"));
        when(cliente.getId()).thenReturn(id);
        when(mockService.updateCliente(anyLong(), any())).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder request = put("/clientes/" + id)
                .content(getGson().toJson(cliente, Cliente.class)).contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void deleteClienteByIdValid() throws Exception {
        MockHttpServletRequestBuilder request = delete("/clientes/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void deleteClienteByIdThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException(0L)).when(mockService).deleteClienteById(anyLong());
        MockHttpServletRequestBuilder request = delete("/clientes/0").contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(request);

        result
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException));
    }

    private Gson getGson() {
        return new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    }
}