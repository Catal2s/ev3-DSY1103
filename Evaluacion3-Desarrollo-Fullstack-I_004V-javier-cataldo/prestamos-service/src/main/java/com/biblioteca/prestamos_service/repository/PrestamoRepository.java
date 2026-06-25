package com.biblioteca.prestamos_service.repository;

import com.biblioteca.prestamos_service.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Prestamo.
 * Proporciona CRUD completo y metodos personalizados de busqueda.
 */
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Busca todos los prestamos asociados a un socio especifico.
     * Spring Data JPA interpreta el nombre del metodo y genera la consulta automaticamente.
     * @param socioId ID del socio
     * @return lista de prestamos del socio
     */
    List<Prestamo> findBySocioId(Long socioId);

    /**
     * Verifica si ya existe un prestamo activo del mismo libro para el mismo socio.
     * Evita prestamos duplicados del mismo libro a la misma persona.
     * @param socioId ID del socio
     * @param libroId ID del libro
     * @return true si ya existe un prestamo activo
     */
    boolean existsBySocioIdAndLibroIdAndActivoTrue(Long socioId, Long libroId);
}
