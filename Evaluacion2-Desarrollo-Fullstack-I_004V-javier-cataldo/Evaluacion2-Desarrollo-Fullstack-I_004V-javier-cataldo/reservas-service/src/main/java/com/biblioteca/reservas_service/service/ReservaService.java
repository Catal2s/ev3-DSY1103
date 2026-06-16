package com.biblioteca.reservas_service.service;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio que contiene la logica de negocio para las reservas.
 * Aqui se aplican las reglas de negocio y se realizan las operaciones
 * sobre el repositorio.
 */
@Service
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);
    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    /**
     * Obtiene todas las reservas registradas en el sistema.
     * @return lista de todas las reservas
     */
    public List<Reserva> obtenerTodas() {
        log.info("Obteniendo todas las reservas");
        return reservaRepository.findAll();
    }

    /**
     * Busca una reserva por su ID. Lanza excepcion si no existe.
     * @param id ID de la reserva
     * @return reserva encontrada
     * @throws RuntimeException si no se encuentra la reserva
     */
    public Reserva obtenerPorId(Long id) {
        log.info("Buscando reserva con id: {}", id);
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    /**
     * Crea una nueva reserva en el sistema.
     * Se asigna automaticamente la fecha actual como fecha de reserva,
     * la fecha de expiracion a 3 dias y el estado inicial como "PENDIENTE".
     * @param reserva datos de la reserva a crear
     * @return reserva creada con sus valores asignados
     */
    public Reserva crearReserva(Reserva reserva) {
        log.info("Creando reserva para socio id: {} y libro id: {}", reserva.getSocioId(), reserva.getLibroId());
        return reservaRepository.save(reserva);
    }

    /**
     * Cancela una reserva cambiando su estado a "CANCELADA".
     * @param id ID de la reserva a cancelar
     * @return reserva cancelada
     */
    public Reserva cancelarReserva(Long id) {
        Reserva reserva = obtenerPorId(id);
        reserva.setEstado("CANCELADA");
        reserva.setActivo(false);
        log.info("Cancelando reserva con id: {}", id);
        return reservaRepository.save(reserva);
    }

    /**
     * Obtiene todas las reservas de un socio especifico.
     * @param socioId ID del socio
     * @return lista de reservas del socio
     */
    public List<Reserva> obtenerPorSocio(Long socioId) {
        log.info("Obteniendo reservas del socio id: {}", socioId);
        return reservaRepository.findBySocioId(socioId);
    }
}
