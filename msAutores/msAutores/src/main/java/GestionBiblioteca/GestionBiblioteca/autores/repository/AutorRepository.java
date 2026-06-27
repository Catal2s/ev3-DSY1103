package GestionBiblioteca.GestionBiblioteca.autores.repository;

import GestionBiblioteca.GestionBiblioteca.autores.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
}