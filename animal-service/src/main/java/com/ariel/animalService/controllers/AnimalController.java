package com.ariel.animalService.controllers;

import com.ariel.animalService.exceptions.ResourceUnavailableException;
import com.ariel.animalService.models.Animal;
import com.ariel.animalService.services.AnimalService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    Logger logger = LoggerFactory.getLogger(AnimalController.class);

    @Autowired
    private AnimalService animalService;


    @GetMapping()
    @CircuitBreaker(name = "getAllAnimais", fallbackMethod = "fallResponse")
    public ResponseEntity<List<Animal>> getAllAnimais() {
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/clientes/{idCliente}")
    public ResponseEntity<List<Animal>> getAllAnimaisFromCliente(@PathVariable long idCliente) {
        return ResponseEntity.ok(animalService.getAllByIdCliente(idCliente));  // retorno nao esta correto, feing - outro metodo sobre a parte
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable long id) {
        return ResponseEntity.ok(animalService.getById(id));
    }

    @PostMapping("/clientes/{idCliente}")
    public ResponseEntity<Animal> insertAnimal(@PathVariable long idCliente, @Valid @RequestBody Animal animal) {
        Animal savedAnimal = animalService.insertAnimalForClienteId(animal, idCliente);
        URI uri = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAnimalById(savedAnimal.getId())).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable long id, @Valid @RequestBody Animal animal) {
        animalService.updateAnimal(id, animal);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimalById(@PathVariable long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Animal>> fallResponse(RuntimeException th) {
        logger.error("Fallback response activated");
        throw new ResourceUnavailableException(th);
    }
}
