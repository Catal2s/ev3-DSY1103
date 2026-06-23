package com.biblioteca.multas_service.controller;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.service.MultaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para el microservicio de Multas.
 * Expone endpoints para gestionar multas por devolucion tardia.
 * Sigue el patron CSR: Controller -> Service -> Repository
 */
@RestController
@RequestMapping("/api/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    /**
     * GET /api/multas
     * Obtiene todas las multas registradas.
     * @return lista de multas con codigo HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Multa>> obtenerTodas() {
        return ResponseEntity.ok(multaService.obtenerTodas());
    }

    /**
     * GET /api/multas/{id}
     * Obtiene una multa por su ID.
     * @param id ID de la multa
     * @return multa encontrada con codigo HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Multa> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.obtenerPorId(id));
    }

    /**
     * GET /api/multas/socio/{socioId}
     * Obtiene todas las multas de un socio especifico.
     * @param socioId ID del socio
     * @return lista de multas del socio con codigo HTTP 200
     */
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<Multa>> obtenerPorSocio(@PathVariable Long socioId) {
        return ResponseEntity.ok(multaService.obtenerPorSocio(socioId));
    }

    /**
     * GET /api/multas/pendientes
     * Obtiene todas las multas que aun no han sido pagadas.
     * @return lista de multas pendientes con codigo HTTP 200
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Multa>> obtenerPendientes() {
        return ResponseEntity.ok(multaService.obtenerPendientes());
    }

    /**
     * POST /api/multas
     * Crea una nueva multa. Los datos son validados automaticamente.
     * @param multa datos de la multa a crear (JSON)
     * @return multa creada con codigo HTTP 200
     */
    @PostMapping
    public ResponseEntity<Multa> crearMulta(@Valid @RequestBody Multa multa) {
        return ResponseEntity.ok(multaService.crearMulta(multa));
    }

    /**
     * PUT /api/multas/{id}/pagar
     * Registra el pago de una multa, cambiando su estado a pagada
     * y asignando la fecha actual como fecha de pago.
     * @param id ID de la multa a pagar
     * @return multa actualizada con codigo HTTP 200
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Multa> pagarMulta(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.pagarMulta(id));
    }
}
