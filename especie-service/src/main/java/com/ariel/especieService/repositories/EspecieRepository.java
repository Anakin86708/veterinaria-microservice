package com.ariel.especieService.repositories;

import com.ariel.especieService.models.Especie;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Cacheable("especie")
public interface EspecieRepository extends JpaRepository<Especie, Long> {

    @Override
    @Cacheable(value = "especie", key = "'especie_' + #aLong")
    Optional<Especie> findById(Long aLong);

    Optional<Especie> findByNome(String nome);

    boolean existsByNome(String nome);
}
