package com.biblioteca.pagos_service.repository;

import com.biblioteca.pagos_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Pago.
 * Proporciona CRUD completo y metodos de busqueda por socio o multa.
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Busca todos los pagos realizados por un socio especifico.
     * @param socioId ID del socio
     * @return lista de pagos del socio
     */
    List<Pago> findBySocioId(Long socioId);

    /**
     * Busca todos los pagos asociados a una multa especifica.
     * @param multaId ID de la multa
     * @return lista de pagos de la multa
     */
    List<Pago> findByMultaId(Long multaId);
}
