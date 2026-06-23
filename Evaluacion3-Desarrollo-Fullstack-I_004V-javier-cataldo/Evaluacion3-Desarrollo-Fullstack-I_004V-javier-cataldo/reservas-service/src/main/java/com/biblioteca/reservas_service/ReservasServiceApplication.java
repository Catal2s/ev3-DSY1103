package com.biblioteca.reservas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Reservas.
 * Spring Boot escanea automaticamente los paquetes controller, service, repository y model
 * al estar dentro del mismo paquete base (com.biblioteca.reservas_service).
 */
@SpringBootApplication
public class ReservasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservasServiceApplication.class, args);
	}

}
