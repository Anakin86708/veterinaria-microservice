package com.ariel.consultaService.services;

import com.ariel.consultaService.exceptions.ResourceNotFoundException;
import com.ariel.consultaService.models.Animal;
import com.ariel.consultaService.models.Consulta;
import com.ariel.consultaService.models.Veterinario;
import com.ariel.consultaService.proxies.AnimalProxy;
import com.ariel.consultaService.proxies.VeterinarioProxy;
import com.ariel.consultaService.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;

    private final AnimalProxy animalProxy;

    private final VeterinarioProxy veterinarioProxy;

    @Autowired
    public ConsultaService(ConsultaRepository repository, AnimalProxy animalProxy, VeterinarioProxy veterinarioProxy) {
        this.repository = repository;
        this.animalProxy = animalProxy;
        this.veterinarioProxy = veterinarioProxy;
    }

    public List<Consulta> getAll() {
        return repository.findAll();
    }

    public Consulta getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Consulta> getConsultasByIdAnimal(long id) {
        Animal animal = animalProxy.retrieveAnimalById(id);
        if (animal != null)
            return repository.findAllByAnimal(animal);
        throw new ResourceNotFoundException(id);
    }

    public List<Consulta> getConsultasByIdVeterinario(long id) {
        Veterinario veterinario = veterinarioProxy.retrieveVeterinarioById(id);
        if (veterinario != null)
            return repository.findAllByVeterinario(veterinario);
        throw new ResourceNotFoundException(id);
    }

    public Consulta insertConsulta(Consulta consulta) {
        consulta.setAnimal(animalProxy.retrieveAnimalById(consulta.getAnimal().getId()));
        consulta.setVeterinario(veterinarioProxy.retrieveVeterinarioById(consulta.getVeterinario().getId()));
        return saveConsulta(consulta);
    }

    public Consulta updateConsulta(long updatedId, Consulta consulta) {
        Consulta consultaDb = getById(updatedId);
        consultaDb.setComentarios(consulta.getComentarios());
        consultaDb.setDataAgendada(consulta.getDataAgendada());
        consultaDb.setFinalizado(consulta.isFinalizado());
        consultaDb.setAnimal(animalProxy.retrieveAnimalById(consulta.getAnimal().getId()));
        consultaDb.setVeterinario(veterinarioProxy.retrieveVeterinarioById(consulta.getVeterinario().getId()));
        return saveConsulta(consultaDb);
    }

    public void deleteConsulta(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private Consulta saveConsulta(Consulta consultaDb) {
        return repository.save(consultaDb);
    }
}
