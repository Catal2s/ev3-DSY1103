package com.biblioteca.pagos_service.service;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.model.Pago;
import com.biblioteca.pagos_service.repository.PagoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    public PagoService(PagoRepository pagoRepository, WebClient webClient) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
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
        //Valida que la multa exista llamando a multas-service
        try {
            JsonNode multaJson = webClient.get()
                    .uri("http://localhost:8085/api/multas/" + request.getMultaId())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (multaJson == null) {
                throw new RuntimeException("La multa con ID " + request.getMultaId() + " no existe.");
            }
            log.info("Multa {} validada correctamente con multas-service", request.getMultaId());
        } catch (Exception e) {
            log.error("Error al validar multa con id: {}", request.getMultaId());
            throw new RuntimeException("No se pudo validar la multa. Asegurate de que multas-service este corriendo.");
        }

        Pago pago = new Pago();
        pago.setMultaId(request.getMultaId());
        pago.setSocioId(request.getSocioId());
        pago.setMonto(request.getMonto());

        log.info("Registrando pago para socio id: {}", request.getSocioId());
        return convertirAResponse(pagoRepository.save(pago));
    }

    public List<PagoResponseDTO> obtenerPorSocio(Long socioId) {
        log.info("Obteniendo pagos del socio id: {}", socioId);
        return pagoRepository.findBySocioId(socioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}
