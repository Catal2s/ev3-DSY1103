package msReportes.msReportes.reportes.controller;

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
public class ReporteController {

    private static final Logger logger = LoggerFactory.getLogger(ReporteController.class);
    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        logger.info("GET /api/reportes");
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReporte(@PathVariable Long id) {
        logger.info("GET /api/reportes/{}", id);
        return ResponseEntity.ok(reporteService.obtenerReportePorId(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> listarPorTipo(@PathVariable String tipo) {
        logger.info("GET /api/reportes/tipo/{}", tipo);
        return ResponseEntity.ok(reporteService.listarPorTipo(tipo));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> listarPorEstado(@PathVariable String estado) {
        logger.info("GET /api/reportes/estado/{}", estado);
        return ResponseEntity.ok(reporteService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@Valid @RequestBody ReporteDTO dto) {
        logger.info("POST /api/reportes");
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crearReporte(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Long id, @Valid @RequestBody ReporteDTO dto) {
        logger.info("PUT /api/reportes/{}", id);
        return ResponseEntity.ok(reporteService.actualizarReporte(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        logger.info("DELETE /api/reportes/{}", id);
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}