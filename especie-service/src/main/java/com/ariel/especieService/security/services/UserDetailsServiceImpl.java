package com.ariel.especieService.security.services;

import com.ariel.especieService.models.User;
import com.ariel.especieService.proxies.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserProxy userProxy;

    @Autowired
    public UserDetailsServiceImpl(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userProxy.getUserFromUsername(username);
        return UserDetailsImpl.build(user);
    }
}
