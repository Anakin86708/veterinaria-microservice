package com.ariel.especieService.proxies;

import com.ariel.especieService.configurations.UserFeingClientConfig;
import com.ariel.especieService.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL:http://localhost}:8600", configuration = UserFeingClientConfig.class)
public interface UserProxy {
    @GetMapping("/users/byName/{username}")
    User getUserFromUsername(@PathVariable String username);
}
