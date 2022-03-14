package com.ariel.veterinariaauth.repositories;

import com.ariel.veterinariaauth.models.ERole;
import com.ariel.veterinariaauth.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Long, Role> {
    Optional<Role> findByName(ERole name);
}
