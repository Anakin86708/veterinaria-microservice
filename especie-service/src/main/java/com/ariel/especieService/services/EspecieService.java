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

    public Especie insertEspecie(Especie especie) {
        if (repository.existsByNome(especie.getNome()))
            throw new DuplicateUniqueResourceException("Espécie with name " + especie.getNome() + " already present");

        return repository.save(especie);
    }

    public Especie updateEspecie(long updatedId, Especie especie) {
        Especie especieDb = getById(updatedId);
        especieDb.setNome(especie.getNome());
        return repository.save(especieDb);
    }

    public void deleteEspecieById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
