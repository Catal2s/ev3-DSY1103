package msReportes.msReportes.reportes.service;

import msReportes.msReportes.reportes.dto.ReporteDTO;
import msReportes.msReportes.reportes.model.Reporte;
import msReportes.msReportes.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;
    private ReporteDTO reporteDTO;

    @BeforeEach
    void setUp() {
        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo("PRESTAMOS");
        reporte.setTitulo("Reporte de Préstamos Junio 2025");
        reporte.setDescripcion("Detalle de todos los préstamos del mes");
        reporte.setFechaGeneracion("2025-06-22");
        reporte.setEstado("GENERADO");

        reporteDTO = new ReporteDTO();
        reporteDTO.setTipo("PRESTAMOS");
        reporteDTO.setTitulo("Reporte de Préstamos Junio 2025");
        reporteDTO.setDescripcion("Detalle de todos los préstamos del mes");
        reporteDTO.setFechaGeneracion("2025-06-22");
        reporteDTO.setEstado("GENERADO");
    }

    // ─── GET /api/reportes ───────────────────────────────────────────────────

    @Test
    void listarReportes_conReportesExistentes_retornaLista() {
        // Given
        when(reporteRepository.findAll()).thenReturn(Arrays.asList(reporte));

        // When
        List<Reporte> resultado = reporteService.listarReportes();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Reporte de Préstamos Junio 2025", resultado.get(0).getTitulo());
        verify(reporteRepository, times(1)).findAll();
    }

    @Test
    void listarReportes_sinReportes_retornaListaVacia() {
        // Given
        when(reporteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Reporte> resultado = reporteService.listarReportes();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reporteRepository, times(1)).findAll();
    }

    // ─── GET /api/reportes/{id} ───────────────────────────────────────────────

    @Test
    void obtenerReportePorId_idExistente_retornaReporte() {
        // Given
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        // When
        Reporte resultado = reporteService.obtenerReportePorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PRESTAMOS", resultado.getTipo());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerReportePorId_idNoExistente_lanzaExcepcion() {
        // Given
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reporteService.obtenerReportePorId(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(reporteRepository, times(1)).findById(99L);
    }

    // ─── GET /api/reportes/tipo/{tipo} ────────────────────────────────────────

    @Test
    void listarPorTipo_tipoExistente_retornaLista() {
        // Given
        when(reporteRepository.findByTipo("PRESTAMOS")).thenReturn(Arrays.asList(reporte));

        // When
        List<Reporte> resultado = reporteService.listarPorTipo("PRESTAMOS");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PRESTAMOS", resultado.get(0).getTipo());
        verify(reporteRepository, times(1)).findByTipo("PRESTAMOS");
    }

    @Test
    void listarPorTipo_tipoSinReportes_retornaListaVacia() {
        // Given
        when(reporteRepository.findByTipo("SOCIOS")).thenReturn(Collections.emptyList());

        // When
        List<Reporte> resultado = reporteService.listarPorTipo("SOCIOS");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reporteRepository, times(1)).findByTipo("SOCIOS");
    }

    // ─── GET /api/reportes/estado/{estado} ────────────────────────────────────

    @Test
    void listarPorEstado_estadoExistente_retornaLista() {
        // Given
        when(reporteRepository.findByEstado("GENERADO")).thenReturn(Arrays.asList(reporte));

        // When
        List<Reporte> resultado = reporteService.listarPorEstado("GENERADO");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("GENERADO", resultado.get(0).getEstado());
        verify(reporteRepository, times(1)).findByEstado("GENERADO");
    }

    @Test
    void listarPorEstado_estadoSinReportes_retornaListaVacia() {
        // Given
        when(reporteRepository.findByEstado("ERROR")).thenReturn(Collections.emptyList());

        // When
        List<Reporte> resultado = reporteService.listarPorEstado("ERROR");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reporteRepository, times(1)).findByEstado("ERROR");
    }

    // ─── POST /api/reportes ───────────────────────────────────────────────────

    @Test
    void crearReporte_datosValidos_retornaReporteCreado() {
        // Given
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        // When
        Reporte resultado = reporteService.crearReporte(reporteDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("PRESTAMOS", resultado.getTipo());
        assertEquals("Reporte de Préstamos Junio 2025", resultado.getTitulo());
        assertEquals("GENERADO", resultado.getEstado());
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void crearReporte_verificaCamposAsignados_correctamente() {
        // Given
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> {
            Reporte r = inv.getArgument(0);
            r.setId(2L);
            return r;
        });

        // When
        Reporte resultado = reporteService.crearReporte(reporteDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Detalle de todos los préstamos del mes", resultado.getDescripcion());
        assertEquals("2025-06-22", resultado.getFechaGeneracion());
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    // ─── PUT /api/reportes/{id} ───────────────────────────────────────────────

    @Test
    void actualizarReporte_idExistente_retornaReporteActualizado() {
        // Given
        reporteDTO.setTitulo("Reporte Actualizado");
        reporteDTO.setEstado("PENDIENTE");
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Reporte resultado = reporteService.actualizarReporte(1L, reporteDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Reporte Actualizado", resultado.getTitulo());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void actualizarReporte_idNoExistente_lanzaExcepcion() {
        // Given
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> reporteService.actualizarReporte(99L, reporteDTO));
        verify(reporteRepository, times(1)).findById(99L);
        verify(reporteRepository, never()).save(any(Reporte.class));
    }

    // ─── DELETE /api/reportes/{id} ────────────────────────────────────────────

    @Test
    void eliminarReporte_idExistente_ejecutaSinErrores() {
        // Given
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        doNothing().when(reporteRepository).deleteById(1L);

        // When
        reporteService.eliminarReporte(1L);

        // Then
        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarReporte_idNoExistente_lanzaExcepcion() {
        // Given
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> reporteService.eliminarReporte(99L));
        verify(reporteRepository, times(1)).findById(99L);
        verify(reporteRepository, never()).deleteById(anyLong());
    }
}
