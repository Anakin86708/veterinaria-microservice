package com.ariel.veterinariaauth.security.services;

import com.ariel.veterinariaauth.exceptions.ResourceNotFoundException;
import com.ariel.veterinariaauth.models.User;
import com.ariel.veterinariaauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(username));
        return UserDetailsImpl.build(user);
    }
}
