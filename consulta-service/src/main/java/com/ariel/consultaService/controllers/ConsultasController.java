package com.ariel.consultaService.controllers;

import com.ariel.consultaService.models.Consulta;
import com.ariel.consultaService.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/consultas")
public class ConsultasController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping()
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        return ResponseEntity.ok(consultaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable long id) {
        return ResponseEntity.ok(consultaService.getById(id));
    }

    @GetMapping("/animal/{id}")
    public ResponseEntity<List<Consulta>> getConsultasByIdAnimal(@PathVariable long id) {
        return ResponseEntity.ok(consultaService.getConsultasByIdAnimal(id));
    }

    @GetMapping("/veterinario/{id}")
    public ResponseEntity<List<Consulta>> getConsultasByIdVeterinario(@PathVariable long id) {
        return ResponseEntity.ok(consultaService.getConsultasByIdVeterinario(id));
    }

    @PostMapping()
    public ResponseEntity<Consulta> insertConsulta(@Valid @RequestBody Consulta consulta) {
        Consulta savedConsulta = consultaService.insertConsulta(consulta);
        URI uri = linkTo(methodOn(this.getClass()).getConsultaById(savedConsulta.getId())).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsultaById(@PathVariable long id, @Valid @RequestBody Consulta consulta) {
        consultaService.updateConsulta(id, consulta);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultaById(@PathVariable long id) {
        consultaService.deleteConsulta(id);
        return ResponseEntity.ok().build();
    }

}
