package GestionBiblioteca.GestionBiblioteca.repository;

import GestionBiblioteca.GestionBiblioteca.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Socio.
 * JpaRepository ya proporciona los metodos CRUD basicos:
 * save(), findAll(), findById(), deleteById(), etc.
 * Aqui se agregan metodos de busqueda personalizados.
 */
@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    /**
     * Verifica si ya existe un socio con el RUT especificado.
     * Se usa para evitar duplicados antes de crear un socio.
     * @param rut RUT a verificar
     * @return true si ya existe, false si no
     */
    boolean existsByRut(String rut);

    /**
     * Verifica si ya existe un socio con el email especificado.
     * Se usa para evitar duplicados de email.
     * @param email email a verificar
     * @return true si ya existe, false si no
     */
    boolean existsByEmail(String email);
}
