package com.ariel.especieService.controllers;

import com.ariel.especieService.models.Especie;
import com.ariel.especieService.services.EspecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/especies")
public class EspecieController {

    @Autowired
    private EspecieService especieService;

    public EspecieController() {
    }

    @GetMapping()
    public ResponseEntity<List<Especie>> getAllEspecies() {
        return ResponseEntity.ok(especieService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especie> getEspecieById(@PathVariable long id) {
        return ResponseEntity.ok(especieService.getById(id));
    }

    @GetMapping("/byName")
    public Especie getEspecieByName(@RequestParam String name) {
        return especieService.getByNome(name);
    }

    @PostMapping()
    public ResponseEntity<Especie> insertEspecie(@Valid @RequestBody Especie especie) {
        Especie save = especieService.insertEspecie(especie);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "especie", key = "'especie_' + #id")
    public ResponseEntity<Especie> updateEspecie(@PathVariable long id, @Valid @RequestBody Especie especie) {
        especieService.updateEspecie(id, especie);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "especie", key = "'especie_' + #id")
    public ResponseEntity<Void> deleteEspecieById(@PathVariable long id) {
        especieService.deleteEspecieById(id);
        return ResponseEntity.ok().build();
    }

}
