package com.ariel.animalService.proxies;

import com.ariel.animalService.models.Especie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "especie-service", url = "${ESPECIE_SERVICE_URL:http://localhost}:8400")
public interface EspecieProxy {
    @GetMapping("/especies/byName/{name}")
    Especie retrieveEspecieByName(@PathVariable String name);
}
