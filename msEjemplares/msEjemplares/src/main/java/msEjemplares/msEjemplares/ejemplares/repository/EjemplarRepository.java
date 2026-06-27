package msEjemplares.msEjemplares.ejemplares.repository;

import msEjemplares.msEjemplares.ejemplares.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    List<Ejemplar> findByLibroId(Long libroId);
}