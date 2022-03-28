package com.ariel.especieService.security.services;

import com.ariel.especieService.models.TokenRequest;
import com.ariel.especieService.models.TokenResponse;
import com.ariel.especieService.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtUtils jwtUtils;


    @Autowired
    public TokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public TokenResponse getToken(TokenRequest tokenRequest) {
        String token = jwtUtils.generateJwtToken(
                new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword())
        );
        return new TokenResponse(token);
    }
}
