package com.biblioteca.prestamos_service.controller;

import com.biblioteca.prestamos_service.dto.PrestamoRequestDTO;
import com.biblioteca.prestamos_service.dto.PrestamoResponseDTO;
import com.biblioteca.prestamos_service.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerPorSocio(@PathVariable Long socioId) {
        return ResponseEntity.ok(prestamoService.obtenerPorSocio(socioId));
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> crearPrestamo(@Valid @RequestBody PrestamoRequestDTO request) {
        return ResponseEntity.ok(prestamoService.crearPrestamo(request));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverPrestamo(id));
    }
}
