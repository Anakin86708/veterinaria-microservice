package com.ariel.veterinarioService.services;

import com.ariel.veterinarioService.exceptions.ActiveForeignKeyException;
import com.ariel.veterinarioService.exceptions.ResourceNotFoundException;
import com.ariel.veterinarioService.models.Veterinario;
import com.ariel.veterinarioService.repositories.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository repository;

    public List<Veterinario> getAll() {
        return repository.findAll();
    }


    public Veterinario getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Veterinario insertVeterinario(Veterinario veterinario) {
        return repository.save(veterinario);
    }

    public Veterinario updateVeterinario(long updatedId, Veterinario veterinario) {
        Veterinario veterinarioDb = getById(updatedId);
        veterinarioDb.setNome(veterinario.getNome());
        veterinarioDb.setEndereco(veterinario.getEndereco());
        veterinarioDb.setTelefone(veterinario.getTelefone());
        return repository.save(veterinarioDb);
    }

    public void deleteVeterinarioById(long id) {
        if (repository.existsById(id)) {
            try {
                repository.deleteById(id);
            } catch (Exception e) {
                throw new ActiveForeignKeyException(id);
            }
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
