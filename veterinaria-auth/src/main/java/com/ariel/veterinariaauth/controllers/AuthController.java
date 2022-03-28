package com.ariel.veterinariaauth.controllers;

import com.ariel.veterinariaauth.models.ERole;
import com.ariel.veterinariaauth.models.Role;
import com.ariel.veterinariaauth.models.User;
import com.ariel.veterinariaauth.payloads.requests.SignInRequest;
import com.ariel.veterinariaauth.payloads.requests.SignupRequest;
import com.ariel.veterinariaauth.payloads.responses.JwtResponse;
import com.ariel.veterinariaauth.payloads.responses.MessageResponse;
import com.ariel.veterinariaauth.repositories.RoleRepository;
import com.ariel.veterinariaauth.repositories.UserRepository;
import com.ariel.veterinariaauth.security.jwt.JwtUtils;
import com.ariel.veterinariaauth.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignupRequest payload) {
        String username = payload.getUsername();
        String email = payload.getEmail();
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Username or email is already in use")
            );
        }

        String password = encoder.encode(payload.getPassword());
        User newUser = new User(username, email, password);

        Set<String> payloadRoles = payload.getRoles();
        Set<Role> roles;
        if (payloadRoles == null) {
            Role role = getRole(ERole.ROLE_USER);
            roles = new HashSet<Role>(
                    List.of(role)
            );
        } else {
            roles = payloadRoles.stream().map(String::toUpperCase).map(ERole::valueOf).map(this::getRole).collect(Collectors.toSet());
        }

        newUser.setRoles(roles);
        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User created!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest payload) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        payload.getUsername(), payload.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwtToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    private Role getRole(ERole user) {
        return roleRepository.findByName(user).orElseThrow(RuntimeException::new);
    }

}
