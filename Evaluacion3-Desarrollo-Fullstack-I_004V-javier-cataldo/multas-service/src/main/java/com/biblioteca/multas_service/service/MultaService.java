package com.biblioteca.multas_service.service;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.repository.MultaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {

    private static final Logger log = LoggerFactory.getLogger(MultaService.class);
    private final MultaRepository multaRepository;
    private final WebClient webClient;
    private final String prestamosServiceUrl;

    public MultaService(MultaRepository multaRepository, WebClient webClient,
                        @Value("${prestamos.service.url:http://localhost:8082}") String prestamosServiceUrl) {
        this.multaRepository = multaRepository;
        this.webClient = webClient;
        this.prestamosServiceUrl = prestamosServiceUrl;
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
        //Regla de negocio: multa duplicada mismo prestamo
        if (multaRepository.existsByPrestamoId(multa.getPrestamoId())) {
            throw new RuntimeException("Ya existe una multa para el prestamo con ID " + multa.getPrestamoId() + ".");
        }

        //Valida que el prestamo exista y este vencido llamando a prestamos-service
        try {
            JsonNode prestamoJson = webClient.get()
                    .uri(prestamosServiceUrl + "/api/prestamos/" + multa.getPrestamoId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (prestamoJson == null) {
                throw new RuntimeException("El prestamo con ID " + multa.getPrestamoId() + " no existe.");
            }

            //Regla de negocio: validar que el prestamo este vencido
            if (prestamoJson.has("fechaDevolucion")) {
                LocalDate fechaDevolucion = LocalDate.parse(prestamoJson.get("fechaDevolucion").asText());
                if (!fechaDevolucion.isBefore(LocalDate.now())) {
                    throw new RuntimeException("El prestamo con ID " + multa.getPrestamoId() + " no esta vencido. Su fecha de devolucion es " + fechaDevolucion + ".");
                }
            }

            log.info("Prestamo {} validado correctamente con prestamos-service", multa.getPrestamoId());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar prestamo con id: {}", multa.getPrestamoId());
            throw new RuntimeException("No se pudo validar el prestamo. Asegurate de que prestamos-service este corriendo.");
        }

        //Regla de negocio: calculo automatico de multa
        //Si el cliente no envio monto, se calcula con valor fijo de 1000 por dia de atraso
        if (multa.getMonto() == null || multa.getMonto() <= 0) {
            try {
                JsonNode prestamoJson = webClient.get()
                        .uri(prestamosServiceUrl + "/api/prestamos/" + multa.getPrestamoId())
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();
                if (prestamoJson != null && prestamoJson.has("fechaDevolucion")) {
                    LocalDate fechaDevolucion = LocalDate.parse(prestamoJson.get("fechaDevolucion").asText());
                    long diasAtraso = LocalDate.now().toEpochDay() - fechaDevolucion.toEpochDay();
                    multa.setMonto((double) diasAtraso * 1000);
                    log.info("Monto calculado automaticamente: {} por {} dias de atraso", multa.getMonto(), diasAtraso);
                }
            } catch (Exception e) {
                log.warn("No se pudo calcular monto automaticamente, se usara el monto proporcionado");
                if (multa.getMonto() == null || multa.getMonto() <= 0) {
                    throw new RuntimeException("El monto de la multa debe ser mayor a cero.");
                }
            }
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
