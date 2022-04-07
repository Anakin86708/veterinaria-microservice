package com.ariel.especieService.controllers;

import com.ariel.especieService.models.TokenRequest;
import com.ariel.especieService.models.TokenResponse;
import com.ariel.especieService.security.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/token")
@CrossOrigin
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    public ResponseEntity<TokenResponse> getToken(@Valid @RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(tokenService.getToken(tokenRequest));
    }
}
