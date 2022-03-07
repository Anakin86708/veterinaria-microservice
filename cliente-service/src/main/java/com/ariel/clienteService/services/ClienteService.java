package com.ariel.clienteService.services;

import com.ariel.clienteService.exceptions.ResourceNotFoundException;
import com.ariel.clienteService.models.Cliente;
import com.ariel.clienteService.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> getAll() {
        return repository.findAll();
    }

    public Cliente getById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Cliente insertCliente(Cliente cliente) {
        return saveCliente(cliente);
    }

    public Cliente updateCliente(long updatedId, Cliente cliente) {
        Cliente clienteDb = getById(updatedId);
        clienteDb.setNome(cliente.getNome());
        clienteDb.setDataNascimento(cliente.getDataNascimento());
        clienteDb.setEndereco(cliente.getEndereco());
        clienteDb.setTelefone(cliente.getTelefone());
        clienteDb.setEmail(cliente.getEmail());
        return saveCliente(clienteDb);
    }

    public void deleteClienteById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private Cliente saveCliente(Cliente clienteDb) {
        return repository.save(clienteDb);
    }
}
