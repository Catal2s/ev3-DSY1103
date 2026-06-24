package com.biblioteca.reservas_service.controller;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para el microservicio de Reservas.
 * Expone endpoints para gestionar reservas de libros.
 * Sigue el patron CSR: Controller -> Service -> Repository
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * GET /api/reservas
     * Obtiene todas las reservas registradas.
     * @return lista de reservas con codigo HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    /**
     * GET /api/reservas/{id}
     * Obtiene una reserva por su ID.
     * @param id ID de la reserva
     * @return reserva encontrada con codigo HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    /**
     * GET /api/reservas/socio/{socioId}
     * Obtiene todas las reservas de un socio especifico.
     * @param socioId ID del socio
     * @return lista de reservas del socio con codigo HTTP 200
     */
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<Reserva>> obtenerPorSocio(@PathVariable Long socioId) {
        return ResponseEntity.ok(reservaService.obtenerPorSocio(socioId));
    }

    /**
     * POST /api/reservas
     * Crea una nueva reserva. Los datos son validados automaticamente
     * mediante las anotaciones de Bean Validation en la entidad.
     * @param reserva datos de la reserva a crear (JSON)
     * @return reserva creada con codigo HTTP 200
     */
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.crearReserva(reserva));
    }

    /**
     * PUT /api/reservas/{id}/cancelar
     * Cancela una reserva cambiando su estado a CANCELADA.
     * @param id ID de la reserva a cancelar
     * @return reserva cancelada con codigo HTTP 200
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }
}
