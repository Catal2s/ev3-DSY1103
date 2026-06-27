package msEjemplares.msEjemplares.ejemplares.service;

import msEjemplares.msEjemplares.ejemplares.dto.EjemplarDTO;
import msEjemplares.msEjemplares.ejemplares.model.Ejemplar;
import msEjemplares.msEjemplares.ejemplares.repository.EjemplarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de EjemplarService")
class EjemplarServiceTest {

    @Mock
    private EjemplarRepository ejemplarRepository;

    @InjectMocks
    private EjemplarService ejemplarService;

    private Ejemplar ejemplar;
    private EjemplarDTO ejemplarDTO;

    @BeforeEach
    void setUp() {
        ejemplar = new Ejemplar();
        ejemplar.setId(1L);
        ejemplar.setCodigo("EJ-0001");
        ejemplar.setLibroId(3L);
        ejemplar.setEstado("DISPONIBLE");
        ejemplar.setUbicacion("Estante A-12");

        ejemplarDTO = new EjemplarDTO();
        ejemplarDTO.setCodigo("EJ-0001");
        ejemplarDTO.setLibroId(3L);
        ejemplarDTO.setEstado("DISPONIBLE");
        ejemplarDTO.setUbicacion("Estante A-12");
    }

    @Test
    @DisplayName("Listar ejemplares debe retornar la lista completa")
    void listarEjemplares_debeRetornarListaDeEjemplares() {
        List<Ejemplar> ejemplares = Arrays.asList(ejemplar, new Ejemplar());
        when(ejemplarRepository.findAll()).thenReturn(ejemplares);

        List<Ejemplar> resultado = ejemplarService.listarEjemplares();

        assertEquals(2, resultado.size());
        verify(ejemplarRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Listar ejemplares por libro debe retornar solo los del libro indicado")
    void listarPorLibro_debeRetornarEjemplaresDelLibro() {
        List<Ejemplar> ejemplares = Arrays.asList(ejemplar);
        when(ejemplarRepository.findByLibroId(3L)).thenReturn(ejemplares);

        List<Ejemplar> resultado = ejemplarService.listarPorLibro(3L);

        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getLibroId());
        verify(ejemplarRepository, times(1)).findByLibroId(3L);
    }

    @Test
    @DisplayName("Obtener ejemplar por ID existente debe retornar el ejemplar")
    void obtenerEjemplarPorId_idExistente_debeRetornarEjemplar() {
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));

        Ejemplar resultado = ejemplarService.obtenerEjemplarPorId(1L);

        assertNotNull(resultado);
        assertEquals("EJ-0001", resultado.getCodigo());
        verify(ejemplarRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener ejemplar por ID inexistente debe lanzar excepción")
    void obtenerEjemplarPorId_idInexistente_debeLanzarExcepcion() {
        when(ejemplarRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ejemplarService.obtenerEjemplarPorId(99L));

        assertTrue(exception.getMessage().contains("Ejemplar no encontrado"));
        verify(ejemplarRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Crear ejemplar debe guardar y retornar el ejemplar creado")
    void crearEjemplar_debeGuardarYRetornarEjemplar() {
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(ejemplar);

        Ejemplar resultado = ejemplarService.crearEjemplar(ejemplarDTO);

        assertNotNull(resultado);
        assertEquals("EJ-0001", resultado.getCodigo());
        assertEquals("DISPONIBLE", resultado.getEstado());
        verify(ejemplarRepository, times(1)).save(any(Ejemplar.class));
    }

    @Test
    @DisplayName("Actualizar ejemplar existente debe modificar y retornar el ejemplar")
    void actualizarEjemplar_idExistente_debeActualizarYRetornarEjemplar() {
        EjemplarDTO dtoActualizado = new EjemplarDTO();
        dtoActualizado.setCodigo("EJ-0001");
        dtoActualizado.setLibroId(3L);
        dtoActualizado.setEstado("PRESTADO");
        dtoActualizado.setUbicacion("Estante A-12");

        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(ejemplar);

        Ejemplar resultado = ejemplarService.actualizarEjemplar(1L, dtoActualizado);

        assertNotNull(resultado);
        assertEquals("PRESTADO", resultado.getEstado());
        verify(ejemplarRepository, times(1)).findById(1L);
        verify(ejemplarRepository, times(1)).save(any(Ejemplar.class));
    }

    @Test
    @DisplayName("Actualizar ejemplar inexistente debe lanzar excepción")
    void actualizarEjemplar_idInexistente_debeLanzarExcepcion() {
        when(ejemplarRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ejemplarService.actualizarEjemplar(99L, ejemplarDTO));

        verify(ejemplarRepository, never()).save(any(Ejemplar.class));
    }

    @Test
    @DisplayName("Eliminar ejemplar existente debe ejecutar la eliminación")
    void eliminarEjemplar_idExistente_debeEliminarCorrectamente() {
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));
        doNothing().when(ejemplarRepository).deleteById(1L);

        ejemplarService.eliminarEjemplar(1L);

        verify(ejemplarRepository, times(1)).findById(1L);
        verify(ejemplarRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar ejemplar inexistente debe lanzar excepción y no eliminar")
    void eliminarEjemplar_idInexistente_debeLanzarExcepcion() {
        when(ejemplarRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ejemplarService.eliminarEjemplar(99L));

        verify(ejemplarRepository, never()).deleteById(anyLong());
    }
}