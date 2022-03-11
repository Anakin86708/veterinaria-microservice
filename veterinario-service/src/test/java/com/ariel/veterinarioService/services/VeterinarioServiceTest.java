package com.ariel.veterinarioService.services;

import com.ariel.veterinarioService.exceptions.ActiveForeignKeyException;
import com.ariel.veterinarioService.exceptions.ResourceNotFoundException;
import com.ariel.veterinarioService.models.Veterinario;
import com.ariel.veterinarioService.repositories.VeterinarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VeterinarioServiceTest {

    private List<Veterinario> veterinarios;
    private VeterinarioRepository repository;

    @BeforeEach
    void setUp() {
        veterinarios = List.of(
                new Veterinario(1L, "Vet1", "a", "123"),
                new Veterinario(2L, "Vet2", "b", "321"),
                new Veterinario(3L, "Vet3", "c", "213")
        );
        repository = mock(VeterinarioRepository.class);
    }

    @Test
    void assertGetAllReturnsAll() {
        when(repository.findAll()).thenReturn(veterinarios);
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        List<Veterinario> result = veterinarioService.getAll();

        assertTrue(result.containsAll(veterinarios));
    }

    @Test
    void assertGetByIdValidForAll() {
        setupFindByIdOnMock();
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        veterinarios.forEach(vet -> assertEquals(vet, veterinarioService.getById(vet.getId())));
    }

    @Test
    void assertGetByIdValidThrowException() {
        setupFindByIdOnMock();
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        assertThrows(ResourceNotFoundException.class, () -> veterinarioService.getById(10L));
    }

    @Test
    void assertInsertVeterinarioWasSaved() {
        Veterinario newVet = new Veterinario(10L, "Vet10", "a", "231");
        when(repository.save(any(Veterinario.class))).thenReturn(newVet);
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        Veterinario result = veterinarioService.insertVeterinario(newVet);

        verify(repository).save(any(Veterinario.class));
        assertEquals(newVet, result);
    }

    @Test
    void assertUpdateVeterinarioWasSaved() {
        long id = 1L;
        Veterinario newVet = new Veterinario(id, "Vet_01", "a", "231");
        when(repository.save(any(Veterinario.class))).thenReturn(newVet);
        setupFindByIdOnMock();
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        Veterinario result = veterinarioService.updateVeterinario(id, newVet);

        verify(repository).save(any(Veterinario.class));
        assertEquals(newVet, result);
    }

    @Test
    void assertUpdateVeterinarioThrowExceptionWhenInvalidId() {
        long id = 10L;
        Veterinario newVet = new Veterinario(id, "Vet_10", "a", "231");
        when(repository.save(any(Veterinario.class))).thenReturn(newVet);
        setupFindByIdOnMock();
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        assertThrows(ResourceNotFoundException.class, () -> veterinarioService.updateVeterinario(id, newVet));
    }

    @Test
    void assertDeleteVeterinarioByIdIsValid() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        veterinarioService.deleteVeterinarioById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assertDeleteVeterinarioByIdThrowExceptionWhenInvalidId() {
        long id = 10L;
        when(repository.existsById(id)).thenReturn(false);
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        assertThrows(ResourceNotFoundException.class, () -> veterinarioService.deleteVeterinarioById(id));
    }

    @Test
    void assertDeleteVeterinarioByIdThrowExceptionWhenActiveForeignKey() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException()).when(repository).deleteById(id);
        VeterinarioService veterinarioService = new VeterinarioService(repository);

        assertThrows(ActiveForeignKeyException.class, () -> veterinarioService.deleteVeterinarioById(id));
    }

    private void setupFindByIdOnMock() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        veterinarios.forEach(vet -> when(repository.findById(vet.getId())).thenReturn(Optional.of(vet)));
    }
}