package com.ariel.animalService.proxies;

import com.ariel.animalService.models.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "${CLIENTE_SERVICE_URL:http://localhost}:8200")
public interface ClienteProxy {
    @GetMapping("/clientes/{id}")
    public Cliente retrieveClienteById(@PathVariable long id);
}
