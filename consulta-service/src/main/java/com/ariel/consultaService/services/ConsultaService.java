package com.ariel.consultaService.services;

import com.ariel.consultaService.exceptions.ResourceNotFoundException;
import com.ariel.consultaService.models.Consulta;
import com.ariel.consultaService.proxies.AnimalProxy;
import com.ariel.consultaService.proxies.VeterinarioProxy;
import com.ariel.consultaService.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private AnimalProxy animalProxy;

    @Autowired
    private VeterinarioProxy veterinarioProxy;

    public List<Consulta> getAll() {
        return repository.findAll();
    }

    public Consulta getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Consulta> getConsultasByIdAnimal(long id) {
        return repository.findAllByAnimal(animalProxy.retrieveAnimalById(id));
    }

    public List<Consulta> getConsultasByIdVeterinario(long id) {
        return repository.findAllByVeterinario(veterinarioProxy.retrieveVeterinarioById(id));
    }

    public Consulta insertConsulta(Consulta consulta) {
        consulta.setAnimal(animalProxy.retrieveAnimalById(consulta.getAnimal().getId()));
        consulta.setVeterinario(veterinarioProxy.retrieveVeterinarioById(consulta.getVeterinario().getId()));
        return repository.save(consulta);
    }

    public Consulta updateConsulta(long updatedId, Consulta consulta) {
        Consulta consultaDb = getById(updatedId);
        consultaDb.setComentarios(consulta.getComentarios());
        consultaDb.setDataAgendada(consulta.getDataAgendada());
        consultaDb.setFinalizado(consulta.isFinalizado());
        consultaDb.setAnimal(animalProxy.retrieveAnimalById(consulta.getAnimal().getId()));
        consultaDb.setVeterinario(veterinarioProxy.retrieveVeterinarioById(consulta.getVeterinario().getId()));
        return repository.save(consultaDb);
    }

    public void deleteConsulta(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
}
