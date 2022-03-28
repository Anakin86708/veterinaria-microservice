package com.ariel.especieService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EspecieServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EspecieServiceApplication.class, args);
    }

}
