package com.ariel.veterinariaConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class VeterinariaConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinariaConfigServerApplication.class, args);
	}

}
