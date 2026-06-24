package com.biblioteca.multas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Multas.
 * Spring Boot escanea automaticamente los paquetes controller, service, repository y model
 * al estar dentro del mismo paquete base (com.biblioteca.multas_service).
 */
@SpringBootApplication
public class MultasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultasServiceApplication.class, args);
	}

}
