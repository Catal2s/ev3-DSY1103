package msNotificaciones.msNotificaciones.notificaciones.service;

import msNotificaciones.msNotificaciones.notificaciones.dto.NotificacionDTO;
import msNotificaciones.msNotificaciones.notificaciones.exception.ResourceNotFoundException;
import msNotificaciones.msNotificaciones.notificaciones.model.Notificacion;
import msNotificaciones.msNotificaciones.notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setMensaje("Su préstamo vence mañana");
        notificacion.setDestinatario("socio1@biblioteca.cl");
        notificacion.setTipo("VENCIMIENTO");
        notificacion.setEstado("PENDIENTE");
        notificacion.setSocioId(5L);
        notificacion.setFechaCreacion(LocalDateTime.now());

        notificacionDTO = new NotificacionDTO();
        notificacionDTO.setMensaje("Su préstamo vence mañana");
        notificacionDTO.setDestinatario("socio1@biblioteca.cl");
        notificacionDTO.setTipo("VENCIMIENTO");
        notificacionDTO.setEstado("PENDIENTE");
        notificacionDTO.setSocioId(5L);
    }

    // ─── GET /api/notificaciones ─────────────────────────────────────────────

    @Test
    void listarNotificaciones_conNotificacionesExistentes_retornaLista() {
        // Given
        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(notificacion));

        // When
        List<Notificacion> resultado = notificacionService.listarNotificaciones();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Su préstamo vence mañana", resultado.get(0).getMensaje());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    void listarNotificaciones_sinNotificaciones_retornaListaVacia() {
        // Given
        when(notificacionRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Notificacion> resultado = notificacionService.listarNotificaciones();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notificacionRepository, times(1)).findAll();
    }

    // ─── GET /api/notificaciones/{id} ────────────────────────────────────────

    @Test
    void obtenerNotificacionPorId_idExistente_retornaNotificacion() {
        // Given
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        // When
        Notificacion resultado = notificacionService.obtenerNotificacionPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("VENCIMIENTO", resultado.getTipo());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerNotificacionPorId_idNoExistente_lanzaResourceNotFoundException() {
        // Given
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> notificacionService.obtenerNotificacionPorId(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(notificacionRepository, times(1)).findById(99L);
    }

    // ─── GET /api/notificaciones/estado/{estado} ──────────────────────────────

    @Test
    void listarPorEstado_estadoExistente_retornaLista() {
        // Given
        when(notificacionRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(notificacion));

        // When
        List<Notificacion> resultado = notificacionService.listarPorEstado("PENDIENTE");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(notificacionRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    void listarPorEstado_estadoSinResultados_retornaListaVacia() {
        // Given
        when(notificacionRepository.findByEstado("ENVIADA")).thenReturn(Collections.emptyList());

        // When
        List<Notificacion> resultado = notificacionService.listarPorEstado("ENVIADA");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notificacionRepository, times(1)).findByEstado("ENVIADA");
    }

    // ─── GET /api/notificaciones/socio/{socioId} ──────────────────────────────

    @Test
    void listarPorSocio_socioConNotificaciones_retornaLista() {
        // Given
        when(notificacionRepository.findBySocioId(5L)).thenReturn(Arrays.asList(notificacion));

        // When
        List<Notificacion> resultado = notificacionService.listarPorSocio(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getSocioId());
        verify(notificacionRepository, times(1)).findBySocioId(5L);
    }

    @Test
    void listarPorSocio_socioSinNotificaciones_retornaListaVacia() {
        // Given
        when(notificacionRepository.findBySocioId(99L)).thenReturn(Collections.emptyList());

        // When
        List<Notificacion> resultado = notificacionService.listarPorSocio(99L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notificacionRepository, times(1)).findBySocioId(99L);
    }

    // ─── POST /api/notificaciones ─────────────────────────────────────────────

    @Test
    void crearNotificacion_datosValidos_retornaNotificacionCreada() {
        // Given
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        // When
        Notificacion resultado = notificacionService.crearNotificacion(notificacionDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Su préstamo vence mañana", resultado.getMensaje());
        assertEquals("socio1@biblioteca.cl", resultado.getDestinatario());
        assertEquals(5L, resultado.getSocioId());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void crearNotificacion_sinEstado_asignaEstadoPorDefecto() {
        // Given
        notificacionDTO.setEstado(null);
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(inv -> {
            Notificacion n = inv.getArgument(0);
            return n;
        });

        // When
        Notificacion resultado = notificacionService.crearNotificacion(notificacionDTO);

        // Then
        assertNotNull(resultado);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    // ─── PUT /api/notificaciones/{id} ─────────────────────────────────────────

    @Test
    void actualizarNotificacion_idExistente_retornaNotificacionActualizada() {
        // Given
        notificacionDTO.setEstado("ENVIADA");
        notificacionDTO.setMensaje("Mensaje actualizado");
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Notificacion resultado = notificacionService.actualizarNotificacion(1L, notificacionDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("ENVIADA", resultado.getEstado());
        assertEquals("Mensaje actualizado", resultado.getMensaje());
        verify(notificacionRepository, times(1)).findById(1L);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void actualizarNotificacion_idNoExistente_lanzaExcepcion() {
        // Given
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> notificacionService.actualizarNotificacion(99L, notificacionDTO));
        verify(notificacionRepository, times(1)).findById(99L);
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    // ─── DELETE /api/notificaciones/{id} ──────────────────────────────────────

    @Test
    void eliminarNotificacion_idExistente_ejecutaSinErrores() {
        // Given
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        doNothing().when(notificacionRepository).delete(notificacion);

        // When
        notificacionService.eliminarNotificacion(1L);

        // Then
        verify(notificacionRepository, times(1)).findById(1L);
        verify(notificacionRepository, times(1)).delete(notificacion);
    }

    @Test
    void eliminarNotificacion_idNoExistente_lanzaExcepcion() {
        // Given
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> notificacionService.eliminarNotificacion(99L));
        verify(notificacionRepository, times(1)).findById(99L);
        verify(notificacionRepository, never()).delete(any(Notificacion.class));
    }
}
