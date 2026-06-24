package com.biblioteca.multas_service.repository;

import com.biblioteca.multas_service.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Multa.
 * Proporciona operaciones CRUD basicas y metodos de busqueda personalizados.
 */
@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    /**
     * Busca todas las multas de un socio especifico.
     * @param socioId ID del socio
     * @return lista de multas del socio
     */
    List<Multa> findBySocioId(Long socioId);

    /**
     * Busca todas las multas de un prestamo especifico.
     * @param prestamoId ID del prestamo
     * @return lista de multas del prestamo
     */
    List<Multa> findByPrestamoId(Long prestamoId);

    /**
     * Busca todas las multas que aun no han sido pagadas.
     * @return lista de multas pendientes
     */
    List<Multa> findByPagadaFalse();
}
