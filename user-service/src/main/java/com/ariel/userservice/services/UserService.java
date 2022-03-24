package com.ariel.userservice.services;

import com.ariel.userservice.exceptions.ResourceNotFoundException;
import com.ariel.userservice.models.User;
import com.ariel.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUserFromUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(username));
    }
}
