package msReportes.msReportes.reportes.service;

import msReportes.msReportes.reportes.dto.ReporteDTO;
import msReportes.msReportes.reportes.model.Reporte;
import msReportes.msReportes.reportes.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);
    private final ReporteRepository reporteRepository;

    public List<Reporte> listarReportes() {
        logger.info("Listando todos los reportes");
        return reporteRepository.findAll();
    }

    public List<Reporte> listarPorTipo(String tipo) {
        logger.info("Listando reportes de tipo: {}", tipo);
        return reporteRepository.findByTipo(tipo);
    }

    public List<Reporte> listarPorEstado(String estado) {
        logger.info("Listando reportes con estado: {}", estado);
        return reporteRepository.findByEstado(estado);
    }

    public Reporte obtenerReportePorId(Long id) {
        logger.info("Buscando reporte con id: {}", id);
        return reporteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reporte con id {} no encontrado", id);
                    return new RuntimeException("Reporte no encontrado con id: " + id);
                });
    }

    public Reporte crearReporte(ReporteDTO dto) {
        logger.info("Creando nuevo reporte de tipo: {}", dto.getTipo());
        Reporte reporte = new Reporte();
        reporte.setTipo(dto.getTipo());
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setFechaGeneracion(dto.getFechaGeneracion());
        reporte.setEstado(dto.getEstado());
        return reporteRepository.save(reporte);
    }

    public Reporte actualizarReporte(Long id, ReporteDTO dto) {
        logger.info("Actualizando reporte con id: {}", id);
        Reporte reporte = obtenerReportePorId(id);
        reporte.setTipo(dto.getTipo());
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setFechaGeneracion(dto.getFechaGeneracion());
        reporte.setEstado(dto.getEstado());
        return reporteRepository.save(reporte);
    }

    public void eliminarReporte(Long id) {
        logger.info("Eliminando reporte con id: {}", id);
        reporteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reporte con id {} no encontrado para eliminar", id);
                    return new RuntimeException("Reporte no encontrado con id: " + id);
                });
        reporteRepository.deleteById(id);
    }
}