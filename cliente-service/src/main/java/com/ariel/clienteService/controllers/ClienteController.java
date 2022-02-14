package com.ariel.clienteService.controllers;

import com.ariel.clienteService.models.Cliente;
import com.ariel.clienteService.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteController methodOnClass = methodOn(this.getClass());
    @Autowired
    private ClienteService clienteService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping
    public CollectionModel<EntityModel<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.getAll();
        CollectionModel<EntityModel<Cliente>> model = CollectionModel.of(
                clientes.stream().map(EntityModel::of)
                        .map(entity -> entity.add(linkTo(methodOnClass.getClienteById(Objects.requireNonNull(entity.getContent()).getId())).withRel("cliente")))
                        .collect(Collectors.toList())
        );
        model.add(linkTo(methodOnClass.getAllClientes()).withSelfRel()
                .andAffordance(afford(methodOnClass.insertCliente(new Cliente()))));
        // TODO: Como vincular ao RootEntryPoint
        //model.add(linkTo(methodOn(RootEntryPointController.class).root()).withRel("root"));
        return model;
    }

    @PostMapping
    public ResponseEntity<Cliente> insertCliente(@Valid @RequestBody Cliente cliente) {
        Cliente save = clienteService.insertCliente(cliente);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public EntityModel<Cliente> getClienteById(@PathVariable long id) {
        Cliente cliente = clienteService.getById(id);
        EntityModel<Cliente> model = EntityModel.of(cliente);
        model.add(
                linkTo(methodOnClass.getClienteById(id)).withSelfRel()
                        .andAffordance(afford(methodOnClass.updateCliente(id, null)))
                        .andAffordance(afford(methodOnClass.deleteClienteById(id)))
        );
        model.add(linkTo(methodOnClass.getAllClientes()).withRel("all-clientes"));
        // TODO: como vincular com métodos em outros serviços
        //model.add(linkTo(methodOn(AnimalController.class).getAllAnimaisFromCliente(cliente.getId())).withRel("animais-pertencentes"));
        return model;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable long id, @Valid @RequestBody Cliente cliente) {
        try {
            clienteService.updateCliente(id, cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteById(@PathVariable long id) {
        try {
            clienteService.deleteClienteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
