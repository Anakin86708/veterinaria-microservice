package com.ariel.veterinarioService.repositories;

import com.ariel.veterinarioService.models.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
}
