package com.ariel.especieService.controllers;

import com.ariel.especieService.exceptions.DuplicateUniqueResourceException;
import com.ariel.especieService.exceptions.ResourceNotFoundException;
import com.ariel.especieService.models.Especie;
import com.ariel.especieService.services.EspecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/especies")
public class EspecieController {

    private final EspecieController methodOnClass;
    @Autowired
    private EspecieService especieService;

    public EspecieController() {
        methodOnClass = methodOn(this.getClass());
    }

    @GetMapping()
    public CollectionModel<EntityModel<Especie>> getAllEspecies() {
        List<Especie> especies = especieService.getAll();
        CollectionModel<EntityModel<Especie>> model = CollectionModel.of(
                especies.stream().map(EntityModel::of)
                        .map(entity -> entity.add(linkTo(methodOnClass.deleteEspecieById(Objects.requireNonNull(entity.getContent()).getId())).withRel("especie")))
                        .collect(Collectors.toList())
        );
        model.add(
                linkTo(methodOnClass.getAllEspecies()).withSelfRel()
                        .andAffordance(afford(methodOnClass.insertEspecie(null)))
        );
        // TODO: Como vincular ao RootEntryPoint
        //model.add(linkTo(methodOn(RootEntryPointController.class).root()).withRel("root"));
        return model;
    }

    @PostMapping()
    public ResponseEntity<Especie> insertEspecie(@Valid @RequestBody Especie especie) {
        Especie save = especieService.insertEspecie(especie);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/{id}")
    public EntityModel<Especie> getEspecieById(@PathVariable long id) {
        Especie especie = especieService.getById(id);
        EntityModel<Especie> model = EntityModel.of(especie);
        model.add(
                linkTo(methodOnClass.getEspecieById(id)).withSelfRel()
                        .andAffordance(afford(methodOnClass.updateEspecie(id, null)))
                        .andAffordance(afford(methodOnClass.deleteEspecieById(id)))
        );
        model.add(linkTo(methodOnClass.getAllEspecies()).withRel("all-especies"));
        return model;
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "especie", key = "'especie_' + #id")
    public ResponseEntity<Especie> updateEspecie(@PathVariable long id, @Valid @RequestBody Especie especie) {
        try {
            especieService.updateEspecie(id, especie);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new DuplicateUniqueResourceException(String.format("Trying to insert an entry with name [%s] that already exists", especie.getNome()));
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "especie", key = "'especie_' + #id")
    public ResponseEntity<Void> deleteEspecieById(@PathVariable long id) {
        try {
            especieService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
