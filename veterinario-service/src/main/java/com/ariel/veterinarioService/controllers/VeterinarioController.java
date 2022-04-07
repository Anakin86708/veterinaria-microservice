package com.ariel.veterinarioService.controllers;

import com.ariel.veterinarioService.models.Veterinario;
import com.ariel.veterinarioService.services.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/veterinarios")
@CrossOrigin(origins = "*")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping()
    public ResponseEntity<List<Veterinario>> getAllVeterinarios() {
        return ResponseEntity.ok(veterinarioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> getVeterinarioById(@PathVariable long id) {
        return ResponseEntity.ok(veterinarioService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Veterinario> insertVeterinario(@Valid @RequestBody Veterinario veterinario) {
        Veterinario save = veterinarioService.insertVeterinario(veterinario);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> updateVeterinario(@PathVariable long id, @Valid @RequestBody Veterinario veterinario) {
        veterinarioService.updateVeterinario(id, veterinario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarioById(@PathVariable long id) {
        veterinarioService.deleteVeterinarioById(id);
        return ResponseEntity.ok().build();
    }
}
