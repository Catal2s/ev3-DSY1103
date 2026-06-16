package com.biblioteca.multas_service.service;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.repository.MultaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {

    private static final Logger log = LoggerFactory.getLogger(MultaService.class);
    private final MultaRepository multaRepository;
    private final WebClient webClient;

    public MultaService(MultaRepository multaRepository, WebClient webClient) {
        this.multaRepository = multaRepository;
        this.webClient = webClient;
    }

    public List<Multa> obtenerTodas() {
        log.info("Obteniendo todas las multas");
        return multaRepository.findAll();
    }

    public Multa obtenerPorId(Long id) {
        log.info("Buscando multa con id: {}", id);
        return multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con id: " + id));
    }

    public Multa crearMulta(Multa multa) {
        //Regla de negocio: el monto de la multa debe ser mayor a 0
        if (multa.getMonto() <= 0) {
            throw new RuntimeException("El monto de la multa debe ser mayor a cero.");
        }

        //Valida que el prestamo exista llamando a prestamos-service
        try {
            JsonNode prestamoJson = webClient.get()
                    .uri("http://localhost:8082/api/prestamos/" + multa.getPrestamoId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (prestamoJson == null) {
                throw new RuntimeException("El prestamo con ID " + multa.getPrestamoId() + " no existe.");
            }
            log.info("Prestamo {} validado correctamente con prestamos-service", multa.getPrestamoId());
        } catch (Exception e) {
            log.error("Error al validar prestamo con id: {}", multa.getPrestamoId());
            throw new RuntimeException("No se pudo validar el prestamo. Asegurate de que prestamos-service este corriendo.");
        }

        log.info("Creando multa para socio id: {} por prestamo id: {}", multa.getSocioId(), multa.getPrestamoId());
        return multaRepository.save(multa);
    }

    public Multa pagarMulta(Long id) {
        Multa multa = obtenerPorId(id);
        multa.setPagada(true);
        multa.setFechaPago(LocalDate.now());
        log.info("Registrando pago de multa con id: {}", id);
        return multaRepository.save(multa);
    }

    public List<Multa> obtenerPorSocio(Long socioId) {
        log.info("Obteniendo multas del socio id: {}", socioId);
        return multaRepository.findBySocioId(socioId);
    }

    public List<Multa> obtenerPendientes() {
        log.info("Obteniendo multas pendientes de pago");
        return multaRepository.findByPagadaFalse();
    }
}
