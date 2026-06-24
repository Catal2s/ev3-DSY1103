package GestionBiblioteca.GestionBiblioteca.controller;

import GestionBiblioteca.GestionBiblioteca.dto.SocioRequestDTO;
import GestionBiblioteca.GestionBiblioteca.dto.SocioResponseDTO;
import GestionBiblioteca.GestionBiblioteca.service.SocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    //GET /api/socios - trae todos los socios
    @GetMapping
    public ResponseEntity<List<SocioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(socioService.obtenerTodos());
    }

    //GET /api/socios/{id} - trae un socio por id
    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.obtenerPorId(id));
    }

    //POST /api/socios - crea un socio nuevo
    @PostMapping
    public ResponseEntity<SocioResponseDTO> crearSocio(@Valid @RequestBody SocioRequestDTO request) {
        return ResponseEntity.ok(socioService.crearSocio(request));
    }

    //PUT /api/socios/{id} - actualiza un socio existente
    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> actualizarSocio(@PathVariable Long id, @Valid @RequestBody SocioRequestDTO request) {
        return ResponseEntity.ok(socioService.actualizarSocio(id, request));
    }

    //DELETE /api/socios/{id} - elimina un socio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }
}
