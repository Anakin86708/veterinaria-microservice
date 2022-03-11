package com.ariel.veterinariaNamingServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class VeterinariaNamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinariaNamingServerApplication.class, args);
	}

}
