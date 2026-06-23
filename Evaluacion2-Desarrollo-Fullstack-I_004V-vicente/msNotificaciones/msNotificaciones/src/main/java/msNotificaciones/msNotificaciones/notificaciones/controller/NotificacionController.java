package msNotificaciones.msNotificaciones.notificaciones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msNotificaciones.msNotificaciones.notificaciones.dto.NotificacionDTO;
import msNotificaciones.msNotificaciones.notificaciones.model.Notificacion;
import msNotificaciones.msNotificaciones.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del sistema")
public class NotificacionController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionController.class);
    private final NotificacionService notificacionService;

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        logger.info("GET /api/notificaciones");
        return ResponseEntity.ok(notificacionService.listarNotificaciones());
    }

    @Operation(summary = "Obtener notificación por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerNotificacion(@PathVariable Long id) {
        logger.info("GET /api/notificaciones/{}", id);
        return ResponseEntity.ok(notificacionService.obtenerNotificacionPorId(id));
    }

    @Operation(summary = "Listar notificaciones por estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> listarPorEstado(@PathVariable String estado) {
        logger.info("GET /api/notificaciones/estado/{}", estado);
        return ResponseEntity.ok(notificacionService.listarPorEstado(estado));
    }

    @Operation(summary = "Listar notificaciones por socio")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones del socio")
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<Notificacion>> listarPorSocio(@PathVariable Long socioId) {
        logger.info("GET /api/notificaciones/socio/{}", socioId);
        return ResponseEntity.ok(notificacionService.listarPorSocio(socioId));
    }

    @Operation(summary = "Crear nueva notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        logger.info("POST /api/notificaciones");
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.crearNotificacion(dto));
    }

    @Operation(summary = "Actualizar notificación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizarNotificacion(@PathVariable Long id, @Valid @RequestBody NotificacionDTO dto) {
        logger.info("PUT /api/notificaciones/{}", id);
        return ResponseEntity.ok(notificacionService.actualizarNotificacion(id, dto));
    }

    @Operation(summary = "Eliminar notificación por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        logger.info("DELETE /api/notificaciones/{}", id);
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
