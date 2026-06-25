package com.biblioteca.pagos_service.service;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.model.Pago;
import com.biblioteca.pagos_service.repository.PagoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository pagoRepository;
    private final WebClient webClient;
    private final String multasServiceUrl;

    public PagoService(PagoRepository pagoRepository, WebClient webClient,
                       @Value("${multas.service.url:http://localhost:8085}") String multasServiceUrl) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
        this.multasServiceUrl = multasServiceUrl;
    }

    private PagoResponseDTO convertirAResponse(Pago p) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(p.getId());
        dto.setMultaId(p.getMultaId());
        dto.setSocioId(p.getSocioId());
        dto.setMonto(p.getMonto());
        dto.setFechaPago(p.getFechaPago());
        dto.setPagado(p.isPagado());
        return dto;
    }

    public List<PagoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        return convertirAResponse(pago);
    }

    public PagoResponseDTO registrarPago(PagoRequestDTO request) {
        try {
            JsonNode multaJson = webClient.get()
                    .uri(multasServiceUrl + "/api/multas/" + request.getMultaId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (multaJson == null) {
                throw new RuntimeException("La multa con ID " + request.getMultaId() + " no existe.");
            }

            if (multaJson.has("pagada") && multaJson.get("pagada").asBoolean()) {
                throw new RuntimeException("La multa con ID " + request.getMultaId() + " ya está pagada.");
            }

            log.info("Multa {} validada correctamente con multas-service", request.getMultaId());
        } catch (Exception e) {
            log.error("Error al validar multa con id: {}", request.getMultaId());
            throw new RuntimeException("No se pudo validar la multa. " + e.getMessage());
        }

        Pago pago = new Pago();
        pago.setMultaId(request.getMultaId());
        pago.setSocioId(request.getSocioId());
        pago.setMonto(request.getMonto());

        log.info("Registrando pago para socio id: {}", request.getSocioId());
        PagoResponseDTO response = convertirAResponse(pagoRepository.save(pago));

        try {
            webClient.put()
                    .uri(multasServiceUrl + "/api/multas/" + request.getMultaId() + "/pagar")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Multa {} marcada como pagada en multas-service", request.getMultaId());
        } catch (Exception e) {
            log.warn("Pago registrado pero no se pudo notificar a multas-service: {}", e.getMessage());
        }

        return response;
    }

    public List<PagoResponseDTO> obtenerPorSocio(Long socioId) {
        log.info("Obteniendo pagos del socio id: {}", socioId);
        return pagoRepository.findBySocioId(socioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}
