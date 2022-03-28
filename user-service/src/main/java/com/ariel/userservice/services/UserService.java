package com.ariel.userservice.services;

import com.ariel.userservice.exceptions.DuplicateUniqueResourceException;
import com.ariel.userservice.exceptions.ResourceNotFoundException;
import com.ariel.userservice.models.User;
import com.ariel.userservice.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User insertUser(User user) {
        return saveUser(user);
    }

    public User updateUser(String username, User user) {
        try {
            User userDb = getUserFromUsername(username);
            userDb.setUsername(user.getUsername());
            userDb.setPassword(user.getPassword());
            userDb.setRoles(user.getRoles());
            return saveUser(userDb);
        } catch (Exception e) {
            throw new DuplicateUniqueResourceException(String.format("Trying to insert an entry with name [%s] that already exists", user.getUsername()));
        }
    }

    public void deleteUser(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent()) {
            repository.delete(optionalUser.get());
        } else {
            throw new ResourceNotFoundException(username);
        }
    }

    private User saveUser(User user) {
        LoggerFactory.getLogger(getClass()).info("Saving user " + user.getUsername());
        return repository.save(user);
    }
}
