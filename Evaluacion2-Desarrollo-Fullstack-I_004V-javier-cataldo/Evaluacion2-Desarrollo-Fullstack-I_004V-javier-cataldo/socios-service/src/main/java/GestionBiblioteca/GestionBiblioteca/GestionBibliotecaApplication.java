package GestionBiblioteca.GestionBiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal del microservicio de Socios.
 * @SpringBootApplication habilita la configuracion automatica de Spring Boot.
 * @EntityScan indica donde estan las entidades JPA.
 * @EnableJpaRepositories indica donde estan los repositorios.
 */
@SpringBootApplication
@EntityScan("GestionBiblioteca.GestionBiblioteca.model")
@EnableJpaRepositories("GestionBiblioteca.GestionBiblioteca.repository")
public class GestionBibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionBibliotecaApplication.class, args);
	}

}