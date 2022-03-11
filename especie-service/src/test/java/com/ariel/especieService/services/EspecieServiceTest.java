package com.ariel.especieService.services;

import com.ariel.especieService.exceptions.DuplicateUniqueResourceException;
import com.ariel.especieService.exceptions.ResourceNotFoundException;
import com.ariel.especieService.models.Especie;
import com.ariel.especieService.repositories.EspecieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EspecieServiceTest {


    private List<Especie> especies;
    private EspecieRepository repository;

    /**
     * Need to setup mocked repository with necessary data.
     */
    @BeforeEach
    void setUp() {
        especies = List.of(new Especie(0L, "Gato"), new Especie(1L, "Gato laranja"), new Especie(2L, "Gato siamês"));
        repository = mock(EspecieRepository.class);
    }

    @Test
    void assertGetAllReturnsAll() {
        when(repository.findAll()).thenReturn(especies);
        EspecieService especieService = new EspecieService(repository);

        List<Especie> result = especieService.getAll();

        assertTrue(result.containsAll(especies));
    }

    @Test
    void assertGetByIdValidForAll() {
        setupFindByIdOnMock();
        EspecieService especieService = new EspecieService(repository);

        especies.forEach(especie -> assertEquals(especie, especieService.getById(especie.getId())));
    }

    @Test
    void assertGetByIdThrowException() {
        setupFindByIdOnMock();
        EspecieService especieService = new EspecieService(repository);

        assertThrows(ResourceNotFoundException.class, () -> especieService.getById(4L));
    }

    @Test
    void assertGetByNomeIsValidForAll() {
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        especies.forEach(especie -> when(repository.findByNome(especie.getNome())).thenReturn(Optional.of(especie)));
        EspecieService especieService = new EspecieService(repository);

        especies.forEach(especie -> assertEquals(especie, especieService.getByNome(especie.getNome())));
    }

    @Test
    void assertGetByNomeThrowException() {
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        especies.forEach(especie -> when(repository.findByNome(especie.getNome())).thenReturn(Optional.of(especie)));
        EspecieService especieService = new EspecieService(repository);

        assertThrows(ResourceNotFoundException.class, () -> especieService.getByNome("Dog"));
    }

    @Test
    void assertInsertEspecieWasSaved() {
        Especie newEspecie = new Especie(3L, "Gato Sphynx");
        when(repository.save(newEspecie)).thenReturn(newEspecie);
        EspecieService especieService = new EspecieService(repository);

        Especie result = especieService.insertEspecie(newEspecie);

        verify(repository).save(newEspecie);
        assertEquals(newEspecie, result);
    }

    @Test
    void assertInsertEspecieThrowExceptionWithDuplicated() {
        Especie newEspecie = new Especie(3L, "Gato Sphynx");
        when(repository.save(newEspecie)).thenReturn(newEspecie);
        when(repository.existsByNome(newEspecie.getNome())).thenReturn(false).thenReturn(true);
        EspecieService especieService = new EspecieService(repository);

        // Insert for the first time
        Especie result = especieService.insertEspecie(newEspecie);
        verify(repository).save(newEspecie);
        assertEquals(newEspecie, result);

        // Insert for the second time and expect it to throw an exception
        assertThrows(DuplicateUniqueResourceException.class, () -> especieService.insertEspecie(newEspecie));
    }

    @Test
    void assertUpdateEspecieWasSaved() {
        Especie newEspecie = new Especie(0L, "Gato padrão");
        when(repository.save(newEspecie)).thenReturn(newEspecie);
        setupFindByIdOnMock();
        EspecieService especieService = new EspecieService(repository);

        Especie result = especieService.updateEspecie(0L, newEspecie);

        verify(repository).save(any(Especie.class));
        assertEquals(newEspecie, result);
    }

    @Test
    void assertUpdateEspecieThrowExceptionWithDuplicated() {
        Especie newEspecie = new Especie(0L, "Gato padrão");
        when(repository.save(newEspecie)).thenReturn(newEspecie);
        when(repository.existsByNome(newEspecie.getNome())).thenReturn(false).thenReturn(true);
        setupFindByIdOnMock();
        EspecieService especieService = new EspecieService(repository);

        // First update should be ok
        Especie result = especieService.updateEspecie(0L, newEspecie);
        verify(repository).save(any(Especie.class));
        assertEquals(newEspecie, result);

        // Second update must throw an exception
        assertThrows(DuplicateUniqueResourceException.class, () -> especieService.updateEspecie(0L, newEspecie));
    }

    @Test
    void assertDeleteEspecieByIdIsValid() {
        long id = 0L;
        when(repository.existsById(id)).thenReturn(true);
        EspecieService especieService = new EspecieService(repository);

        especieService.deleteEspecieById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void assertDeleteEspecieByIdThrowException() {
        long id = 0L;
        when(repository.existsById(id)).thenReturn(false);
        EspecieService especieService = new EspecieService(repository);

        assertThrows(ResourceNotFoundException.class, () -> especieService.deleteEspecieById(id));
    }

    private void setupFindByIdOnMock() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        especies.forEach(especie -> when(repository.findById(especie.getId())).thenReturn(Optional.of(especie)));
    }
}