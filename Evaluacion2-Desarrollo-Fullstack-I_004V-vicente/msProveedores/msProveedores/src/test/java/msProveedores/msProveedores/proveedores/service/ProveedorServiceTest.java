package msProveedores.msProveedores.proveedores.service;

import msProveedores.msProveedores.proveedores.dto.ProveedorDTO;
import msProveedores.msProveedores.proveedores.model.Proveedor;
import msProveedores.msProveedores.proveedores.repository.ProveedorRepository;
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
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    private Proveedor proveedor;
    private ProveedorDTO proveedorDTO;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Editorial Académica S.A.");
        proveedor.setRut("76.123.456-7");
        proveedor.setEmail("contacto@editorial.cl");
        proveedor.setTelefono("+56912345678");
        proveedor.setDireccion("Av. Providencia 1234, Santiago");
        proveedor.setActivo(true);

        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setNombre("Editorial Académica S.A.");
        proveedorDTO.setRut("76.123.456-7");
        proveedorDTO.setEmail("contacto@editorial.cl");
        proveedorDTO.setTelefono("+56912345678");
        proveedorDTO.setDireccion("Av. Providencia 1234, Santiago");
        proveedorDTO.setActivo(true);
    }

    // ─── GET /api/proveedores ────────────────────────────────────────────────

    @Test
    void listarProveedores_conProveedoresExistentes_retornaLista() {
        // Given
        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(proveedor));

        // When
        List<Proveedor> resultado = proveedorService.listarProveedores();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Editorial Académica S.A.", resultado.get(0).getNombre());
        verify(proveedorRepository, times(1)).findAll();
    }

    @Test
    void listarProveedores_sinProveedores_retornaListaVacia() {
        // Given
        when(proveedorRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Proveedor> resultado = proveedorService.listarProveedores();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(proveedorRepository, times(1)).findAll();
    }

    // ─── GET /api/proveedores/activos ─────────────────────────────────────────

    @Test
    void listarProveedoresActivos_conActivos_retornaLista() {
        // Given
        when(proveedorRepository.findByActivoTrue()).thenReturn(Arrays.asList(proveedor));

        // When
        List<Proveedor> resultado = proveedorService.listarProveedoresActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        verify(proveedorRepository, times(1)).findByActivoTrue();
    }

    @Test
    void listarProveedoresActivos_sinActivos_retornaListaVacia() {
        // Given
        when(proveedorRepository.findByActivoTrue()).thenReturn(Collections.emptyList());

        // When
        List<Proveedor> resultado = proveedorService.listarProveedoresActivos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(proveedorRepository, times(1)).findByActivoTrue();
    }

    // ─── GET /api/proveedores/{id} ────────────────────────────────────────────

    @Test
    void obtenerProveedorPorId_idExistente_retornaProveedor() {
        // Given
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        // When
        Proveedor resultado = proveedorService.obtenerProveedorPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("76.123.456-7", resultado.getRut());
        verify(proveedorRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerProveedorPorId_idNoExistente_lanzaExcepcion() {
        // Given
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> proveedorService.obtenerProveedorPorId(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(proveedorRepository, times(1)).findById(99L);
    }

    // ─── POST /api/proveedores ────────────────────────────────────────────────

    @Test
    void crearProveedor_rutNuevo_retornaProveedorCreado() {
        // Given
        when(proveedorRepository.existsByRut("76.123.456-7")).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        // When
        Proveedor resultado = proveedorService.crearProveedor(proveedorDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Editorial Académica S.A.", resultado.getNombre());
        assertEquals("76.123.456-7", resultado.getRut());
        verify(proveedorRepository, times(1)).existsByRut("76.123.456-7");
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void crearProveedor_rutDuplicado_lanzaExcepcion() {
        // Given
        when(proveedorRepository.existsByRut("76.123.456-7")).thenReturn(true);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> proveedorService.crearProveedor(proveedorDTO));
        assertTrue(ex.getMessage().contains("76.123.456-7"));
        verify(proveedorRepository, times(1)).existsByRut("76.123.456-7");
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    // ─── PUT /api/proveedores/{id} ────────────────────────────────────────────

    @Test
    void actualizarProveedor_idExistente_retornaProveedorActualizado() {
        // Given
        proveedorDTO.setNombre("Editorial Actualizada");
        proveedorDTO.setTelefono("+56999999999");
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Proveedor resultado = proveedorService.actualizarProveedor(1L, proveedorDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Editorial Actualizada", resultado.getNombre());
        assertEquals("+56999999999", resultado.getTelefono());
        verify(proveedorRepository, times(1)).findById(1L);
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void actualizarProveedor_idNoExistente_lanzaExcepcion() {
        // Given
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> proveedorService.actualizarProveedor(99L, proveedorDTO));
        verify(proveedorRepository, times(1)).findById(99L);
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    // ─── DELETE /api/proveedores/{id} ─────────────────────────────────────────

    @Test
    void eliminarProveedor_idExistente_ejecutaSinErrores() {
        // Given
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        doNothing().when(proveedorRepository).deleteById(1L);

        // When
        proveedorService.eliminarProveedor(1L);

        // Then
        verify(proveedorRepository, times(1)).findById(1L);
        verify(proveedorRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarProveedor_idNoExistente_lanzaExcepcion() {
        // Given
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> proveedorService.eliminarProveedor(99L));
        verify(proveedorRepository, times(1)).findById(99L);
        verify(proveedorRepository, never()).deleteById(anyLong());
    }
}
