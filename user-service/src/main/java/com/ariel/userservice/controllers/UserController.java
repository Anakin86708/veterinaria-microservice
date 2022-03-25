package com.ariel.userservice.controllers;

import com.ariel.userservice.models.User;
import com.ariel.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/byName/{username}")
    public ResponseEntity<User> getUserFromUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getUserFromUsername(username));
    }

    @PostMapping()
    public ResponseEntity<User> insertUser(@Valid @RequestBody User user) {
        User save = service.insertUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/byName/{name}").buildAndExpand(save.getUsername()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/byName/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @Valid @RequestBody User user) {
        service.updateUser(username, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/byName/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        service.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
