package com.ariel.consultaService.services;

import com.ariel.consultaService.exceptions.ResourceNotFoundException;
import com.ariel.consultaService.models.*;
import com.ariel.consultaService.proxies.AnimalProxy;
import com.ariel.consultaService.proxies.VeterinarioProxy;
import com.ariel.consultaService.repositories.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ConsultaServiceTest {

    private List<Consulta> consultas;
    private ConsultaRepository repository;
    private AnimalProxy animalProxy;
    private VeterinarioProxy veterinarioProxy;
    private HashMap<Long, Cliente> clientesMap;
    private HashMap<Long, Veterinario> veterinariosMap;
    private HashMap<Long, Animal> animaisMap;

    @BeforeEach
    void setUp() {
        // Cliente setup
        List<Cliente> clientes = List.of(
                new Cliente(1L, "Cliente1", Date.from(Instant.now()), "a", "123", "e@mail.com"),
                new Cliente(2L, "Cliente2", Date.from(Instant.now()), "b", "321", "e@mail.net")
        );
        clientesMap = new HashMap<>();
        clientes.forEach(c -> clientesMap.put(c.getId(), c));

        // Animais setup
        List<Animal> animals = List.of(
                new Animal(1L, "Animal1", Date.from(Instant.now()), Sexo.FEMEA, new Especie(1L, "Gato"), clientesMap.get(2L)),
                new Animal(2L, "Animal2", Date.from(Instant.now()), Sexo.MACHO, new Especie(2L, "Gato Siamês"), clientesMap.get(1L))
        );
        animaisMap = new HashMap<>();
        animals.forEach(a -> animaisMap.put(a.getId(), a));

        // Veterinários setup
        List<Veterinario> veterinarios = List.of(
                new Veterinario(1L, "Vet1", "a", "123"),
                new Veterinario(2L, "Vet2", "b", "321")
        );
        veterinariosMap = new HashMap<>();
        veterinarios.forEach(v -> veterinariosMap.put(v.getId(), v));

        consultas = List.of(
                new Consulta(1L, Date.from(Instant.now()), "a", animaisMap.get(2L), veterinariosMap.get(1L), false),
                new Consulta(2L, Date.from(Instant.now()), "b", animaisMap.get(1L), veterinariosMap.get(2L), false),
                new Consulta(3L, Date.from(Instant.now()), "c", animaisMap.get(1L), veterinariosMap.get(1L), true)
        );

        repository = mock(ConsultaRepository.class);
        animalProxy = mock(AnimalProxy.class);
        veterinarioProxy = mock(VeterinarioProxy.class);
    }

    @Test
    void assertGetAllReturnsAll() {
        when(repository.findAll()).thenReturn(consultas);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        List<Consulta> result = consultaService.getAll();

        assertTrue(result.containsAll(consultas));
    }

    @Test
    void assertGetByIdForAll() {
        setupFindByIdOnMock();
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        consultas.forEach(consulta -> assertEquals(consulta, consultaService.getById(consulta.getId())));
    }

    @Test
    void assertGetByIdThrowException() {
        setupFindByIdOnMock();
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        assertThrows(ResourceNotFoundException.class, () -> consultaService.getById(10L));
    }

    @Test
    void assertGetConsultasByIdAnimalReturnsAll() {
        when(repository.findAllByAnimal(any())).thenReturn(List.of());
        ArrayList<Long> idsAnimais = new ArrayList<>(animaisMap.keySet());
        setupFindAllAllByAnimalOnMock(idsAnimais);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        idsAnimais.forEach(id -> {
            List<Consulta> result = consultaService.getConsultasByIdAnimal(id);

            assertTrue(result.containsAll(consultasFromAnimalWithId(id)));
        });
    }

    @Test
    void assertGetConsultasByIdAnimalThrowException() {
        when(repository.findAllByAnimal(any())).thenReturn(List.of());
        ArrayList<Long> idsAnimais = new ArrayList<>(animaisMap.keySet());
        setupFindAllAllByAnimalOnMock(idsAnimais);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        assertThrows(ResourceNotFoundException.class, () -> consultaService.getConsultasByIdAnimal(10L));
    }

    @Test
    void assertGetConsultasByIdVeterinarioReturnsAll() {
        when(repository.findAllByVeterinario(any())).thenReturn(List.of());
        ArrayList<Long> idsVeterinarios = new ArrayList<>(veterinariosMap.keySet());
        setupFindAllAllByVeterinarioOnMock(idsVeterinarios);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        idsVeterinarios.forEach(id -> {
            List<Consulta> result = consultaService.getConsultasByIdVeterinario(id);

            assertTrue(result.containsAll(consultasFromVeterinarioWithId(id)));
        });
    }

    @Test
    void assertGetConsultasByIdVeterinarioThrowException() {
        when(repository.findAllByVeterinario(any())).thenReturn(List.of());
        ArrayList<Long> idsVeterinarios = new ArrayList<>(veterinariosMap.keySet());
        setupFindAllAllByVeterinarioOnMock(idsVeterinarios);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        assertThrows(ResourceNotFoundException.class, () -> consultaService.getConsultasByIdVeterinario(10L));
    }

    @Test
    void assertInsertConsultaWasSaved() {
        Cliente clientePertencente = new Cliente(10L, "CLiente10", Date.from(Instant.now()), "a", "123", "e@mail.org");
        Animal animal = new Animal(10L, "Animal10", Date.from(Instant.now()), Sexo.MACHO, new Especie(10L, "Gato Sphynx"), clientePertencente);
        Consulta newConsulta = new Consulta(10L, Date.from(Instant.now()), "a", animal, new Veterinario(10L, "Vet10", "b", "213"), false);
        when(repository.save(any(Consulta.class))).thenReturn(newConsulta);
        setupFindByIdOnMock();
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        Consulta result = consultaService.insertConsulta(newConsulta);

        verify(repository).save(any(Consulta.class));
        assertEquals(newConsulta, result);
    }

    @Test
    void assertUpdateConsultaWasSaved() {
        long idConsulta = 1L;
        Cliente clientePertencente = new Cliente(1L, "CLiente10", Date.from(Instant.now()), "a", "123", "e@mail.org");
        Animal animal = new Animal(1L, "Animal10", Date.from(Instant.now()), Sexo.MACHO, new Especie(10L, "Gato Sphynx"), clientePertencente);
        Consulta newConsulta = new Consulta(idConsulta, Date.from(Instant.now()), "a", animal, new Veterinario(10L, "Vet10", "b", "213"), false);
        when(repository.save(any(Consulta.class))).thenReturn(newConsulta);
        setupFindByIdOnMock();
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        Consulta result = consultaService.updateConsulta(idConsulta, newConsulta);

        verify(repository).save(any(Consulta.class));
        assertEquals(newConsulta, result);
    }

    @Test
    void assertUpdateConsultaThrowException() {
        long idConsulta = 10L;
        Cliente clientePertencente = new Cliente(1L, "CLiente10", Date.from(Instant.now()), "a", "123", "e@mail.org");
        Animal animal = new Animal(1L, "Animal10", Date.from(Instant.now()), Sexo.MACHO, new Especie(10L, "Gato Sphynx"), clientePertencente);
        Consulta newConsulta = new Consulta(idConsulta, Date.from(Instant.now()), "a", animal, new Veterinario(10L, "Vet10", "b", "213"), false);
        when(repository.save(any(Consulta.class))).thenReturn(newConsulta);
        setupFindByIdOnMock();
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        assertThrows(ResourceNotFoundException.class, () -> consultaService.updateConsulta(idConsulta, newConsulta));
    }

    @Test
    void assertDeleteConsultaIsValid() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        consultaService.deleteConsulta(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assertDeleteConsultaThrowException() {
        long id = 10L;
        when(repository.existsById(id)).thenReturn(false);
        ConsultaService consultaService = new ConsultaService(repository, animalProxy, veterinarioProxy);

        assertThrows(ResourceNotFoundException.class, () -> consultaService.deleteConsulta(id));
    }

    private void setupFindByIdOnMock() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        consultas.forEach(c -> when(repository.findById(c.getId())).thenReturn(Optional.of(c)));
    }

    private void setupFindAllAllByAnimalOnMock(ArrayList<Long> idsAnimais) {
        idsAnimais.forEach(idAnimal -> {
            Animal animal = animaisMap.get(idAnimal);
            when(animalProxy.retrieveAnimalById(idAnimal)).thenReturn(animal);
            when(repository.findAllByAnimal(animal)).thenReturn(consultasFromAnimalWithId(idAnimal));
        });
    }

    /**
     * Filtra as consultas, obtendo apenas aquelas que forem do mesmo id do animal.
     *
     * @param id Id desejado para o animal.
     * @return Lista com consultas do animal.
     */
    private List<Consulta> consultasFromAnimalWithId(Long id) {
        return consultas.stream().filter(consulta -> Objects.equals(consulta.getAnimal().getId(), id)).collect(Collectors.toList());
    }

    private void setupFindAllAllByVeterinarioOnMock(ArrayList<Long> idsVeterinarios) {
        idsVeterinarios.forEach(idVeterinario -> {
            Veterinario veterinario = veterinariosMap.get(idVeterinario);
            when(veterinarioProxy.retrieveVeterinarioById(idVeterinario)).thenReturn(veterinario);
            when(repository.findAllByVeterinario(veterinario)).thenReturn(consultasFromVeterinarioWithId(idVeterinario));
        });
    }

    /**
     * Filtra as consultas, obtendo apenas aquelas que forem do mesmo id do veterinário.
     *
     * @param id Id desejado para o veterinário.
     * @return Lista com consultas do veterinário.
     */
    private List<Consulta> consultasFromVeterinarioWithId(Long id) {
        return consultas.stream().filter(consulta -> Objects.equals(consulta.getVeterinario().getId(), id)).collect(Collectors.toList());
    }
}