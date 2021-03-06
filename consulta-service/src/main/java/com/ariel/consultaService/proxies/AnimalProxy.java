package com.ariel.consultaService.proxies;

import com.ariel.consultaService.models.Animal;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "animal-service", url = "${ANIMAL_SERVICE_URL:http://localhost}:8100")
public interface AnimalProxy {
    @GetMapping("/animais/{id}")
    @Retry(name = "animais")
    Animal retrieveAnimalById(@PathVariable long id);
}
