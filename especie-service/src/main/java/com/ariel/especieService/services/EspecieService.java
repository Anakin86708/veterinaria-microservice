package com.ariel.especieService.services;

import com.ariel.especieService.exceptions.DuplicateUniqueResourceException;
import com.ariel.especieService.exceptions.ResourceNotFoundException;
import com.ariel.especieService.models.Especie;
import com.ariel.especieService.repositories.EspecieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecieService {

    @Autowired
    private EspecieRepository repository;

    public List<Especie> getAll() {
        return repository.findAll();
    }


    public Especie getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Especie getByNome(String name) {
        return repository.findByNome(name).orElseThrow(() -> new ResourceNotFoundException(name));
    }

    public Especie insertEspecie(Especie especie) {
        if (repository.existsByNome(especie.getNome()))
            throw new DuplicateUniqueResourceException("Espécie with name " + especie.getNome() + " already present");

        return saveEspecie(especie);
    }

    public Especie updateEspecie(long updatedId, Especie especie) {
        try {
            Especie especieDb = getById(updatedId);
            especieDb.setNome(especie.getNome());
            return saveEspecie(especieDb);
        } catch (Exception e) {
            throw new DuplicateUniqueResourceException(
                    String.format("Trying to insert an entry with name [%s] that already exists", especie.getNome())
            );
        }
    }

    public void deleteEspecieById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private Especie saveEspecie(Especie especieDb) {
        return repository.save(especieDb);
    }
}
