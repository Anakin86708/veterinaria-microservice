package com.ariel.animalService.repositories;

import com.ariel.animalService.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findAllByClientePertencenteId(long id);

    Animal findByClientePertencenteIdAndId(long clientePertencenteId, long id);
}
