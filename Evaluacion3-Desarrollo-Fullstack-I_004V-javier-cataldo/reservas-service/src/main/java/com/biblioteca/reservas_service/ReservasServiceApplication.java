package com.biblioteca.reservas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal del microservicio de Reservas.
 * Spring Boot escanea automaticamente los paquetes controller, service, repository y model
 * al estar dentro del mismo paquete base (com.biblioteca.reservas_service).
 */
@SpringBootApplication
@EnableScheduling
public class ReservasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservasServiceApplication.class, args);
	}

}
