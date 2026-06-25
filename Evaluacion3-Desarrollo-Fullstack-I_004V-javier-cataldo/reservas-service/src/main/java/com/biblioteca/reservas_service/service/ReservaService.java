package com.biblioteca.reservas_service.service;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.repository.ReservaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
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
    private final WebClient webClient;
    private final String sociosServiceUrl;

    public ReservaService(ReservaRepository reservaRepository, WebClient webClient,
                          @Value("${socios.service.url:http://localhost:8081}") String sociosServiceUrl) {
        this.reservaRepository = reservaRepository;
        this.webClient = webClient;
        this.sociosServiceUrl = sociosServiceUrl;
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
        //Regla de negocio: validar que el socio existe
        try {
            JsonNode socioJson = webClient.get()
                    .uri(sociosServiceUrl + "/api/socios/" + reserva.getSocioId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (socioJson == null) {
                throw new RuntimeException("El socio con ID " + reserva.getSocioId() + " no existe.");
            }
            log.info("Socio {} validado correctamente con socios-service", reserva.getSocioId());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar socio con id: {}", reserva.getSocioId());
            throw new RuntimeException("No se pudo validar el socio. Asegurate de que socios-service este corriendo.");
        }

        //Regla de negocio: reserva duplicada mismo socio+libro
        if (reservaRepository.existsBySocioIdAndLibroIdAndActivoTrue(reserva.getSocioId(), reserva.getLibroId())) {
            throw new RuntimeException("El socio con ID " + reserva.getSocioId() + " ya tiene una reserva activa del libro con ID " + reserva.getLibroId() + ".");
        }

        log.info("Creando reserva para socio id: {} y libro id: {}", reserva.getSocioId(), reserva.getLibroId());
        return reservaRepository.save(reserva);
    }

    /**
     * Tarea programada que se ejecuta cada minuto para auto-expiracion de reservas.
     * Cambia el estado de reservas PENDIENTE cuya fecha de expiracion ya paso a "EXPIRADA".
     */
    @Scheduled(fixedRate = 60000)
    public void autoExpiracionReservas() {
        List<Reserva> expiradas = reservaRepository.findByEstadoAndFechaExpiracionBefore("PENDIENTE", LocalDate.now());
        for (Reserva reserva : expiradas) {
            reserva.setEstado("EXPIRADA");
            reserva.setActivo(false);
            reservaRepository.save(reserva);
            log.info("Reserva {} auto-expirada por fecha de expiracion vencida", reserva.getId());
        }
        if (!expiradas.isEmpty()) {
            log.info("Se auto-expiraron {} reservas", expiradas.size());
        }
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
