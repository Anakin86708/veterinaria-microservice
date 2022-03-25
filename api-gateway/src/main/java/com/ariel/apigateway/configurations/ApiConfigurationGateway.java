package com.ariel.apigateway.configurations;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfigurationGateway {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/animais/**").uri("lb://animal-service"))
                .route(p -> p.path("/clientes/**").uri("lb://cliente-service"))
                .route(p -> p.path("/especies/**").uri("lb://especie-service"))
                .route(p -> p.path("/veterinarios/**").uri("lb://veterinario-service"))
                .route(p -> p.path("/consultas/**").uri("lb://consulta-service"))
                .route(p -> p.path("/users/**").uri("lb://user-service"))
                .build();
    }
}
