package msCompras.msCompras.compras.service;

import msCompras.msCompras.compras.dto.CompraDTO;
import msCompras.msCompras.compras.model.Compra;
import msCompras.msCompras.compras.repository.CompraRepository;
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
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraService compraService;

    private Compra compra;
    private CompraDTO compraDTO;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        compra.setId(1L);
        compra.setProveedorId(10L);
        compra.setEstado("PENDIENTE");
        compra.setTotal(150000.0);
        compra.setFechaCompra("2025-06-01");
        compra.setDescripcion("Compra de libros técnicos");

        compraDTO = new CompraDTO();
        compraDTO.setProveedorId(10L);
        compraDTO.setEstado("PENDIENTE");
        compraDTO.setTotal(150000.0);
        compraDTO.setFechaCompra("2025-06-01");
        compraDTO.setDescripcion("Compra de libros técnicos");
    }

    // ─── GET /api/compras ────────────────────────────────────────────────────

    @Test
    void listarCompras_conComprasExistentes_retornaLista() {
        // Given
        when(compraRepository.findAll()).thenReturn(Arrays.asList(compra));

        // When
        List<Compra> resultado = compraService.listarCompras();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(compraRepository, times(1)).findAll();
    }

    @Test
    void listarCompras_sinCompras_retornaListaVacia() {
        // Given
        when(compraRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Compra> resultado = compraService.listarCompras();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(compraRepository, times(1)).findAll();
    }

    // ─── GET /api/compras/{id} ────────────────────────────────────────────────

    @Test
    void obtenerCompraPorId_idExistente_retornaCompra() {
        // Given
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));

        // When
        Compra resultado = compraService.obtenerCompraPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(150000.0, resultado.getTotal());
        verify(compraRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerCompraPorId_idNoExistente_lanzaExcepcion() {
        // Given
        when(compraRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.obtenerCompraPorId(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(compraRepository, times(1)).findById(99L);
    }

    // ─── GET /api/compras/proveedor/{proveedorId} ─────────────────────────────

    @Test
    void listarPorProveedor_proveedorConCompras_retornaLista() {
        // Given
        when(compraRepository.findByProveedorId(10L)).thenReturn(Arrays.asList(compra));

        // When
        List<Compra> resultado = compraService.listarPorProveedor(10L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getProveedorId());
        verify(compraRepository, times(1)).findByProveedorId(10L);
    }

    @Test
    void listarPorProveedor_proveedorSinCompras_retornaListaVacia() {
        // Given
        when(compraRepository.findByProveedorId(99L)).thenReturn(Collections.emptyList());

        // When
        List<Compra> resultado = compraService.listarPorProveedor(99L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(compraRepository, times(1)).findByProveedorId(99L);
    }

    // ─── GET /api/compras/estado/{estado} ─────────────────────────────────────

    @Test
    void listarPorEstado_estadoExistente_retornaLista() {
        // Given
        when(compraRepository.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(compra));

        // When
        List<Compra> resultado = compraService.listarPorEstado("PENDIENTE");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(compraRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    void listarPorEstado_estadoSinCompras_retornaListaVacia() {
        // Given
        when(compraRepository.findByEstado("ENTREGADA")).thenReturn(Collections.emptyList());

        // When
        List<Compra> resultado = compraService.listarPorEstado("ENTREGADA");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(compraRepository, times(1)).findByEstado("ENTREGADA");
    }

    // ─── POST /api/compras ────────────────────────────────────────────────────

    @Test
    void crearCompra_datosValidos_retornaCompraCreada() {
        // Given
        when(compraRepository.save(any(Compra.class))).thenReturn(compra);

        // When
        Compra resultado = compraService.crearCompra(compraDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getProveedorId());
        assertEquals(150000.0, resultado.getTotal());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    @Test
    void crearCompra_verificaCamposAsignados_correctamente() {
        // Given
        when(compraRepository.save(any(Compra.class))).thenAnswer(inv -> {
            Compra c = inv.getArgument(0);
            c.setId(2L);
            return c;
        });

        // When
        Compra resultado = compraService.crearCompra(compraDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("2025-06-01", resultado.getFechaCompra());
        assertEquals("Compra de libros técnicos", resultado.getDescripcion());
        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    // ─── PUT /api/compras/{id} ────────────────────────────────────────────────

    @Test
    void actualizarCompra_idExistente_retornaCompraActualizada() {
        // Given
        compraDTO.setEstado("APROBADA");
        compraDTO.setTotal(200000.0);
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(compraRepository.save(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Compra resultado = compraService.actualizarCompra(1L, compraDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("APROBADA", resultado.getEstado());
        assertEquals(200000.0, resultado.getTotal());
        verify(compraRepository, times(1)).findById(1L);
        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    @Test
    void actualizarCompra_idNoExistente_lanzaExcepcion() {
        // Given
        when(compraRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> compraService.actualizarCompra(99L, compraDTO));
        verify(compraRepository, times(1)).findById(99L);
        verify(compraRepository, never()).save(any(Compra.class));
    }

    // ─── DELETE /api/compras/{id} ─────────────────────────────────────────────

    @Test
    void eliminarCompra_idExistente_ejecutaSinErrores() {
        // Given
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        doNothing().when(compraRepository).deleteById(1L);

        // When
        compraService.eliminarCompra(1L);

        // Then
        verify(compraRepository, times(1)).findById(1L);
        verify(compraRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarCompra_idNoExistente_lanzaExcepcion() {
        // Given
        when(compraRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> compraService.eliminarCompra(99L));
        verify(compraRepository, times(1)).findById(99L);
        verify(compraRepository, never()).deleteById(anyLong());
    }
}
