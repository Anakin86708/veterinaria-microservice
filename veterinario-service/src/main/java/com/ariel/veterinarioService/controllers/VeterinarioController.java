package com.ariel.veterinarioService.controllers;

import com.ariel.veterinarioService.models.Veterinario;
import com.ariel.veterinarioService.services.VeterinarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private Logger logger = LoggerFactory.getLogger(VeterinarioController.class);

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping()
    public CollectionModel<EntityModel<Veterinario>> getAllVeterinarios() {
        List<Veterinario> veterinarios = veterinarioService.getAll();
        CollectionModel<EntityModel<Veterinario>> model = CollectionModel.of(
                veterinarios.stream().map(EntityModel::of)
                        .map(entity -> entity.add(linkTo(methodOn(this.getClass()).getVeterinarioById(Objects.requireNonNull(entity.getContent()).getId())).withRel("veterinario")))
                        .collect(Collectors.toList())
        );
        model.add(
                linkTo(methodOn(this.getClass()).getAllVeterinarios()).withSelfRel()
                        .andAffordance(afford(methodOn(this.getClass()).insertVeterinario(null)))
        );
        // TODO: Como vincular ao RootEntryPoint
        //model.add(linkTo(methodOn(RootEntryPointController.class).root()).withRel("root"));

        return model;
    }

    @PostMapping()
    public ResponseEntity<Veterinario> insertVeterinario(@Valid @RequestBody Veterinario veterinario) {
        Veterinario save = veterinarioService.insertVeterinario(veterinario);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public EntityModel<Veterinario> getVeterinarioById(@PathVariable long id) {
        Veterinario veterinario = veterinarioService.getById(id);
        EntityModel<Veterinario> model = EntityModel.of(veterinario);

        Link selfRel = linkTo(methodOn(this.getClass()).getVeterinarioById(id)).withSelfRel();
        model.add(selfRel
                .andAffordance(afford(methodOn(this.getClass()).insertVeterinario(null)))
                .andAffordance(afford(methodOn(this.getClass()).updateVeterinario(id, null)))
                .andAffordance(afford(methodOn(this.getClass()).deleteVeterinarioById(id)))
        );
        model.add(linkTo(methodOn(this.getClass()).getAllVeterinarios()).withRel("veterinarios"));

        return model;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> updateVeterinario(@PathVariable long id, @Valid @RequestBody Veterinario veterinario) {
        veterinarioService.updateVeterinario(id, veterinario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarioById(@PathVariable long id) {
        try {
            veterinarioService.deleteVeterinarioById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
