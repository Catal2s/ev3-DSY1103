package com.biblioteca.pagos_service.controller;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorSocio(@PathVariable Long socioId) {
        return ResponseEntity.ok(pagoService.obtenerPorSocio(socioId));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(@Valid @RequestBody PagoRequestDTO request) {
        return ResponseEntity.ok(pagoService.registrarPago(request));
    }
}
