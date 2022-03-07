package com.ariel.clienteService.repositories;

import com.ariel.clienteService.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
