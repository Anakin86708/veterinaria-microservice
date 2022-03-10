package com.ariel.clienteService.services;

import com.ariel.clienteService.exceptions.ResourceNotFoundException;
import com.ariel.clienteService.models.Cliente;
import com.ariel.clienteService.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private List<Cliente> clientes;
    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        clientes = List.of(
                new Cliente(1L, "Cliente1", Date.from(Instant.now()), "a", "123", "e@mail.com"),
                new Cliente(2L, "Cliente2", Date.from(Instant.now()), "b", "321", "e@mail.net"),
                new Cliente(3L, "Cliente3", Date.from(Instant.now()), "c", "132", "e@mail.br")
        );
        repository = mock(ClienteRepository.class);
    }

    @Test
    void assertGetAllReturnsAll() {
        when(repository.findAll()).thenReturn(clientes);
        ClienteService clienteService = new ClienteService(repository);

        List<Cliente> result = clienteService.getAll();

        assertTrue(result.containsAll(clientes));
    }

    @Test
    void assertGetByIdValidForAll() {
        setupFindByIdOnMock();
        ClienteService clienteService = new ClienteService(repository);

        clientes.forEach(cliente -> assertEquals(cliente, clienteService.getById(cliente.getId())));
    }

    @Test
    void assertGetByIdThrowException() {
        setupFindByIdOnMock();
        ClienteService clienteService = new ClienteService(repository);

        assertThrows(ResourceNotFoundException.class, () -> clienteService.getById(10L));
    }

    @Test
    void insertClienteWasSaved() {
        Cliente newCliente = new Cliente(10L, "Cliente10", Date.from(Instant.now()), "d", "312", "e@mail.org");
        when(repository.save(any(Cliente.class))).thenReturn(newCliente);
        ClienteService clienteService = new ClienteService(repository);

        Cliente result = clienteService.insertCliente(newCliente);

        verify(repository).save(any(Cliente.class));
        assertEquals(newCliente, result);
    }

    @Test
    void assertUpdateClienteWasSaved() {
        long updatedId = 1L;
        Cliente newCliente = new Cliente(updatedId, "Cliente01", Date.from(Instant.now()), "d", "312", "e@mail.org");
        when(repository.save(any(Cliente.class))).thenReturn(newCliente);
        setupFindByIdOnMock();
        ClienteService clienteService = new ClienteService(repository);

        Cliente result = clienteService.updateCliente(updatedId, newCliente);

        verify(repository).save(any(Cliente.class));
        assertEquals(newCliente, result);
    }

    @Test
    void assertUpdateClienteThrowExceptionWhenInvalidId() {
        long updatedId = 10L;
        Cliente newCliente = new Cliente(updatedId, "Cliente10", Date.from(Instant.now()), "d", "312", "e@mail.org");
        when(repository.save(any(Cliente.class))).thenReturn(newCliente);
        setupFindByIdOnMock();
        ClienteService clienteService = new ClienteService(repository);

        assertThrows(ResourceNotFoundException.class, () -> clienteService.updateCliente(updatedId, newCliente));
    }

    @Test
    void assertDeleteClienteByIdIsValid() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        ClienteService clienteService = new ClienteService(repository);

        clienteService.deleteClienteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assertDeleteClienteByIdThrowExceptionWhenInvalidId() {
        long id = 10L;
        when(repository.existsById(id)).thenReturn(false);
        ClienteService clienteService = new ClienteService(repository);

        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteClienteById(id));
    }

    private void setupFindByIdOnMock() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        clientes.forEach(cliente -> when(repository.findById(cliente.getId())).thenReturn(Optional.of(cliente)));
    }
}