package com.ariel.userservice.controllers;

import com.ariel.userservice.models.User;
import com.ariel.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
