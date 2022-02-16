package com.ariel.consultaService.repositories;

import com.ariel.consultaService.models.Animal;
import com.ariel.consultaService.models.Consulta;
import com.ariel.consultaService.models.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findAllByAnimal(Animal animal);

    List<Consulta> findAllByVeterinario(Veterinario retrieveVeterinarioById);
}