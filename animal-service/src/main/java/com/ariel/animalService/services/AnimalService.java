package com.ariel.animalService.services;

import com.ariel.animalService.exceptions.ResourceNotFoundException;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.models.Sexo;
import com.ariel.animalService.proxies.ClienteProxy;
import com.ariel.animalService.proxies.EspecieProxy;
import com.ariel.animalService.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    private final EspecieProxy especieProxy;

    private final ClienteProxy clienteProxy;

    @Autowired
    public AnimalService(AnimalRepository repository, EspecieProxy especieProxy, ClienteProxy clienteProxy) {
        this.repository = repository;
        this.especieProxy = especieProxy;
        this.clienteProxy = clienteProxy;
    }

    public List<Animal> getAll() {
        return repository.findAll();
    }

    public Animal getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Animal> getAllByIdCliente(long idCliente) {
        if (clienteProxy.retrieveClienteById(idCliente).getId() != null)
            return repository.findAllByClientePertencenteId(idCliente);
        throw new ResourceNotFoundException(idCliente);
    }

    public Animal insertAnimalForClienteId(Animal animal, long idCliente) {
        animal.setEspecie(especieProxy.retrieveEspecieByName(animal.getEspecie().getNome()));
        animal.setClientePertencente(clienteProxy.retrieveClienteById(idCliente));
        return saveAnimal(animal);
    }

    public Animal updateAnimal(long updatedId, Animal animal) {
        Animal animalBd = getById(updatedId);
        animalBd.setNome(animal.getNome());
        animalBd.setDataNascimento(animal.getDataNascimento());
        if (animal.getEspecie() != null)
            animalBd.setEspecie(especieProxy.retrieveEspecieByName(animal.getEspecie().getNome()));
        if (animal.getClientePertencente() != null)
            animalBd.setClientePertencente(clienteProxy.retrieveClienteById(animal.getClientePertencente().getId()));
        if (animal.getSexo() != null)
            animalBd.setSexo(Sexo.valueOf(animal.getSexo().name()));
        return saveAnimal(animalBd);
    }

    public void deleteAnimal(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private Animal saveAnimal(Animal animalBd) {
        return repository.save(animalBd);
    }
}
