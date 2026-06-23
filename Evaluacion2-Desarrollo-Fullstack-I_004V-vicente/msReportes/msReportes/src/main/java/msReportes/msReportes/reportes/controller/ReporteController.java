package msReportes.msReportes.reportes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msReportes.msReportes.reportes.dto.ReporteDTO;
import msReportes.msReportes.reportes.model.Reporte;
import msReportes.msReportes.reportes.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Generación y gestión de reportes del sistema")
public class ReporteController {

    private static final Logger logger = LoggerFactory.getLogger(ReporteController.class);
    private final ReporteService reporteService;

    @Operation(summary = "Listar todos los reportes")
    @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        logger.info("GET /api/reportes");
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @Operation(summary = "Obtener reporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReporte(@PathVariable Long id) {
        logger.info("GET /api/reportes/{}", id);
        return ResponseEntity.ok(reporteService.obtenerReportePorId(id));
    }

    @Operation(summary = "Listar reportes por tipo")
    @ApiResponse(responseCode = "200", description = "Lista de reportes filtrada por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> listarPorTipo(@PathVariable String tipo) {
        logger.info("GET /api/reportes/tipo/{}", tipo);
        return ResponseEntity.ok(reporteService.listarPorTipo(tipo));
    }

    @Operation(summary = "Listar reportes por estado")
    @ApiResponse(responseCode = "200", description = "Lista de reportes filtrada por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> listarPorEstado(@PathVariable String estado) {
        logger.info("GET /api/reportes/estado/{}", estado);
        return ResponseEntity.ok(reporteService.listarPorEstado(estado));
    }

    @Operation(summary = "Crear nuevo reporte")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@Valid @RequestBody ReporteDTO dto) {
        logger.info("POST /api/reportes");
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crearReporte(dto));
    }

    @Operation(summary = "Actualizar reporte existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Long id, @Valid @RequestBody ReporteDTO dto) {
        logger.info("PUT /api/reportes/{}", id);
        return ResponseEntity.ok(reporteService.actualizarReporte(id, dto));
    }

    @Operation(summary = "Eliminar reporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reporte eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        logger.info("DELETE /api/reportes/{}", id);
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}
