package com.biblioteca.prestamos_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Prestamos.
 * @SpringBootApplication habilita la configuracion automatica de Spring Boot,
 * el escaneo de componentes y la configuracion de JPA.
 */
@SpringBootApplication
public class PrestamosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrestamosServiceApplication.class, args);
	}

}
