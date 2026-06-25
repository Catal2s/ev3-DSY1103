package com.biblioteca.prestamos_service.service;

import com.biblioteca.prestamos_service.dto.PrestamoRequestDTO;
import com.biblioteca.prestamos_service.dto.PrestamoResponseDTO;
import com.biblioteca.prestamos_service.model.Prestamo;
import com.biblioteca.prestamos_service.repository.PrestamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    private static final Logger log = LoggerFactory.getLogger(PrestamoService.class);
    private final PrestamoRepository prestamoRepository;
    private final WebClient webClient;
    private final String sociosServiceUrl;
    private final String multasServiceUrl;

    public PrestamoService(PrestamoRepository prestamoRepository, WebClient webClient,
                           @Value("${socios.service.url:http://localhost:8081}") String sociosServiceUrl,
                           @Value("${multas.service.url:http://localhost:8085}") String multasServiceUrl) {
        this.prestamoRepository = prestamoRepository;
        this.webClient = webClient;
        this.sociosServiceUrl = sociosServiceUrl;
        this.multasServiceUrl = multasServiceUrl;
    }

    private PrestamoResponseDTO convertirAResponse(Prestamo p) {
        PrestamoResponseDTO dto = new PrestamoResponseDTO();
        dto.setId(p.getId());
        dto.setSocioId(p.getSocioId());
        dto.setLibroId(p.getLibroId());
        dto.setFechaPrestamo(p.getFechaPrestamo());
        dto.setFechaDevolucion(p.getFechaDevolucion());
        dto.setActivo(p.isActivo());
        return dto;
    }

    public List<PrestamoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los prestamos");
        return prestamoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public PrestamoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando prestamo con id: {}", id);
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado con id: " + id));
        return convertirAResponse(prestamo);
    }

    public PrestamoResponseDTO crearPrestamo(PrestamoRequestDTO request) {
        //Regla de negocio: la fecha de devolucion no puede ser anterior a hoy
        if (request.getFechaDevolucion().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de devolucion no puede ser anterior a la fecha actual.");
        }

        //Regla de negocio: mismo libro ya prestado al socio
        if (prestamoRepository.existsBySocioIdAndLibroIdAndActivoTrue(request.getSocioId(), request.getLibroId())) {
            throw new RuntimeException("El socio con ID " + request.getSocioId() + " ya tiene un prestamo activo del libro con ID " + request.getLibroId() + ".");
        }

        //Llama al microservicio de socios pa validar que el socio existe y esta activo
        try {
            JsonNode socioJson = webClient.get()
                    .uri(sociosServiceUrl + "/api/socios/" + request.getSocioId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (socioJson == null || !socioJson.has("activo") || !socioJson.get("activo").asBoolean()) {
                throw new RuntimeException("El socio con ID " + request.getSocioId() + " no esta activo o no existe.");
            }
            log.info("Socio {} validado correctamente con socios-service", request.getSocioId());
        } catch (Exception e) {
            log.error("Error al validar socio con id: {}", request.getSocioId());
            throw new RuntimeException("No se pudo validar el socio. Asegurate de que socios-service este corriendo.");
        }

        //Regla de negocio: socio sin multas pendientes
        try {
            JsonNode multasJson = webClient.get()
                    .uri(multasServiceUrl + "/api/multas/socio/" + request.getSocioId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (multasJson != null && multasJson.isArray()) {
                for (int i = 0; i < multasJson.size(); i++) {
                    JsonNode multa = multasJson.get(i);
                    if (multa.has("pagada") && !multa.get("pagada").asBoolean()) {
                        throw new RuntimeException("El socio con ID " + request.getSocioId() + " tiene multas pendientes. Debe pagarlas antes de solicitar un nuevo prestamo.");
                    }
                }
            }
            log.info("Multas del socio {} validadas correctamente con multas-service", request.getSocioId());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.warn("No se pudo validar multas pendientes para socio {}: {}", request.getSocioId(), e.getMessage());
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setSocioId(request.getSocioId());
        prestamo.setLibroId(request.getLibroId());
        prestamo.setFechaDevolucion(request.getFechaDevolucion());

        log.info("Creando prestamo para socio id: {}", request.getSocioId());
        return convertirAResponse(prestamoRepository.save(prestamo));
    }

    public PrestamoResponseDTO devolverPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado con id: " + id));
        prestamo.setActivo(false);
        log.info("Devolviendo prestamo con id: {}", id);
        return convertirAResponse(prestamoRepository.save(prestamo));
    }

    public List<PrestamoResponseDTO> obtenerPorSocio(Long socioId) {
        log.info("Obteniendo prestamos del socio id: {}", socioId);
        return prestamoRepository.findBySocioId(socioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}
