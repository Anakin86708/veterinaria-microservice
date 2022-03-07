package com.ariel.consultaService.proxies;

import com.ariel.consultaService.models.Veterinario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "veterinario-service", url = "${VETERINARIO_SERVICE_URL:http://localhost}:8500")
public interface VeterinarioProxy {
    @GetMapping("/veterinarios/{id}")
    Veterinario retrieveVeterinarioById(@PathVariable long id);
}
