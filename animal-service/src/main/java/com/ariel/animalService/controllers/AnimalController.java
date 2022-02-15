package com.ariel.animalService.controllers;

import com.ariel.animalService.exceptions.ResourceNotFoundException;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalService animalService;


    @GetMapping()
    public ResponseEntity<List<Animal>> getAllAnimais() {
        List<Animal> animals = animalService.getAll();
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/clientes/{idCliente}")
    public ResponseEntity<List<Animal>> getAllAnimaisFromCliente(@PathVariable long idCliente) {
        List<Animal> allByClientePertencenteId = animalService.getAllByIdCliente(idCliente);
        return ResponseEntity.ok(allByClientePertencenteId);
    }

    @PostMapping("/clientes/{idCliente}")
    public ResponseEntity<Animal> insertAnimal(@PathVariable long idCliente, @Valid @RequestBody Animal animal) {
        Animal savedAnimal = animalService.insertAnimalForClienteId(animal,idCliente );
        URI uri = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAnimalById(savedAnimal.getId())).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable long id) {
        Animal animal = animalService.getById(id);
        return ResponseEntity.ok(animal);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable long id, @Valid @RequestBody Animal animal) {
        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return ResponseEntity.ok(updatedAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimalById(@PathVariable long id) {
        try {
            animalService.deleteAnimal(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
