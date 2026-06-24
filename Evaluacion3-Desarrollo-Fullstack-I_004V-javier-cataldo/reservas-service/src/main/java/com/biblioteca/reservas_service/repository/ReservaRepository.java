package com.biblioteca.reservas_service.repository;

import com.biblioteca.reservas_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Reserva.
 * Proporciona operaciones CRUD basicas y metodos de busqueda personalizados.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Busca todas las reservas asociadas a un socio especifico.
     * @param socioId ID del socio
     * @return lista de reservas del socio
     */
    List<Reserva> findBySocioId(Long socioId);

    /**
     * Busca todas las reservas activas de un libro especifico.
     * @param libroId ID del libro
     * @return lista de reservas activas del libro
     */
    List<Reserva> findByLibroIdAndActivoTrue(Long libroId);
}
