package com.ariel.animalService.services;

import com.ariel.animalService.exceptions.ResourceNotFoundException;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.models.Cliente;
import com.ariel.animalService.models.Especie;
import com.ariel.animalService.models.Sexo;
import com.ariel.animalService.proxies.ClienteProxy;
import com.ariel.animalService.proxies.EspecieProxy;
import com.ariel.animalService.repositories.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AnimalServiceTest {

    private List<Animal> animais;
    private AnimalRepository repository;
    private EspecieProxy especieProxy;
    private ClienteProxy clienteProxy;
    private HashMap<Long, Cliente> clientesMap;

    @BeforeEach
    void setUp() {
        List<Cliente> clientes = List.of(
                new Cliente(1L, "Cliente 1", Date.from(Instant.now()), "end", "123", "e@mail.com"),
                new Cliente(2L, "Cliente 2", Date.from(Instant.now()), "end", "321", "e@mail.net")
        );
        clientesMap = new HashMap<>();
        clientes.forEach(cliente -> clientesMap.put(cliente.getId(), cliente));

        animais = List.of(
                new Animal(1L, "Gato 1", Date.from(Instant.now()), Sexo.FEMEA, new Especie(), clientesMap.get(1L)),
                new Animal(2L, "Gato 2", Date.from(Instant.now()), Sexo.FEMEA, new Especie(), clientesMap.get(1L)),
                new Animal(3L, "Gato 3", Date.from(Instant.now()), Sexo.MACHO, new Especie(), clientesMap.get(2L))
        );
        repository = mock(AnimalRepository.class);
        especieProxy = mock(EspecieProxy.class);
        clienteProxy = mock(ClienteProxy.class);
    }

    @Test
    void assertGetAllReturnsAll() {
        when(repository.findAll()).thenReturn(animais);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        List<Animal> result = animalService.getAll();

        assertTrue(result.containsAll(animais));
    }

    @Test
    void assertGetByIdForAll() {
        setupFindByIdOnMock();
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        animais.forEach(animal -> assertEquals(animal, animalService.getById(animal.getId())));
    }

    @Test
    void assertGetByIdThrowException() {
        setupFindByIdOnMock();
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        assertThrows(ResourceNotFoundException.class, () -> animalService.getById(10L));
    }

    @Test
    void assertGetAllByIdClienteReturnsAll() {
        when(repository.findAllByClientePertencenteId(anyLong())).thenReturn(List.of());
        List<Long> idsClientes = new ArrayList<>(clientesMap.keySet());
        idsClientes.forEach(id -> when(clienteProxy.retrieveClienteById(id)).thenReturn(clientesMap.get(id)));
        HashMap<Long, List<Animal>> idClienteToAnimal = mapIdCLienteToAnimais(idsClientes);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        idsClientes.forEach(id -> {
            List<Animal> result = animalService.getAllByIdCliente(id);

            assertTrue(result.containsAll(idClienteToAnimal.get(id)));
        });
    }

    @Test
    void assertGetAllByIdClienteThrowException() {
        when(repository.findAllByClientePertencenteId(anyLong())).thenReturn(List.of());
        when(clienteProxy.retrieveClienteById(anyLong())).thenThrow(ResourceNotFoundException.class);
        List<Long> idsClientes = animais.stream().map(Animal::getClientePertencente).map(Cliente::getId).distinct().collect(Collectors.toList());
        mapIdCLienteToAnimais(idsClientes);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        assertThrows(ResourceNotFoundException.class, () -> animalService.getAllByIdCliente(10L));
    }

    @Test
    void assertInsertAnimalForClienteIdWasSaved() {
        long idCliente = 2L;
        Animal newAnimal = new Animal(10L, "Gato 10", Date.from(Instant.now()), Sexo.FEMEA, new Especie(), clientesMap.get(idCliente));
        when(repository.save(newAnimal)).thenReturn(newAnimal);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        Animal result = animalService.insertAnimalForClienteId(newAnimal, idCliente);

        verify(repository).save(newAnimal);
        assertEquals(newAnimal, result);
    }

    @Test
    void assertUpdateAnimalWasSaved() {
        long updatedId = 2L;
        Animal newAnimal = new Animal(updatedId, "Gato 10", Date.from(Instant.now()), Sexo.FEMEA, new Especie(), clientesMap.get(2L));
        when(repository.save(any(Animal.class))).thenReturn(newAnimal);
        setupFindByIdOnMock();
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        Animal result = animalService.updateAnimal(updatedId, newAnimal);

        verify(repository).save(any(Animal.class));
        assertEquals(newAnimal, result);
    }

    @Test
    void assertUpdateAnimalThrowExceptionWhenInvalidId() {
        long updatedId = 10L;
        Animal newAnimal = new Animal(updatedId, "Gato 10", Date.from(Instant.now()), Sexo.FEMEA, new Especie(), clientesMap.get(2L));
        when(repository.save(any(Animal.class))).thenReturn(newAnimal);
        setupFindByIdOnMock();
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        assertThrows(ResourceNotFoundException.class, () -> animalService.updateAnimal(updatedId, newAnimal));
    }

    @Test
    void assertDeleteAnimalIsValid() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);

        animalService.deleteAnimal(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assertDeleteAnimalThrowException() {
        long id = 10L;
        when(repository.existsById(id)).thenReturn(false);
        AnimalService animalService = new AnimalService(repository, especieProxy, clienteProxy);


        assertThrows(ResourceNotFoundException.class, () -> animalService.deleteAnimal(id));
    }

    private void setupFindByIdOnMock() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        animais.forEach(animal -> when(repository.findById(animal.getId())).thenReturn(Optional.of(animal)));
    }

    private HashMap<Long, List<Animal>> mapIdCLienteToAnimais(List<Long> idsClientes) {
        HashMap<Long, List<Animal>> idCliente2Animal = new HashMap<>();
        idsClientes.forEach(id -> {
            List<Animal> animaisPertencentesId = animais.stream().filter(animal -> Objects.equals(animal.getClientePertencente().getId(), id)).collect(Collectors.toList());
            idCliente2Animal.put(id, animaisPertencentesId);
            when(repository.findAllByClientePertencenteId(id)).thenReturn(animaisPertencentesId);
        });
        return idCliente2Animal;
    }
}