package msCategoria.msCategoria.categorias.service;

import msCategoria.msCategoria.categorias.dto.CategoriaDTO;
import msCategoria.msCategoria.categorias.model.Categoria;
import msCategoria.msCategoria.categorias.repository.CategoriaRepository;
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
@DisplayName("Pruebas unitarias de CategoriaService")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Novela");
        categoria.setDescripcion("Obras de ficción narrativa");

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Novela");
        categoriaDTO.setDescripcion("Obras de ficción narrativa");
    }

    @Test
    @DisplayName("Listar categorías debe retornar la lista completa")
    void listarCategorias_debeRetornarListaDeCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria, new Categoria());
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.listarCategorias();

        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener categoría por ID existente debe retornar la categoría")
    void obtenerCategoriaPorId_idExistente_debeRetornarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.obtenerCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Novela", resultado.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener categoría por ID inexistente debe lanzar excepción")
    void obtenerCategoriaPorId_idInexistente_debeLanzarExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoriaService.obtenerCategoriaPorId(99L));

        assertTrue(exception.getMessage().contains("Categoria no encontrada"));
        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Crear categoría debe guardar y retornar la categoría creada")
    void crearCategoria_debeGuardarYRetornarCategoria() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.crearCategoria(categoriaDTO);

        assertNotNull(resultado);
        assertEquals("Novela", resultado.getNombre());
        assertEquals("Obras de ficción narrativa", resultado.getDescripcion());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Actualizar categoría existente debe modificar y retornar la categoría")
    void actualizarCategoria_idExistente_debeActualizarYRetornarCategoria() {
        CategoriaDTO dtoActualizado = new CategoriaDTO();
        dtoActualizado.setNombre("Novela Histórica");
        dtoActualizado.setDescripcion("Obras ambientadas en hechos históricos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.actualizarCategoria(1L, dtoActualizado);

        assertNotNull(resultado);
        assertEquals("Novela Histórica", resultado.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Actualizar categoría inexistente debe lanzar excepción")
    void actualizarCategoria_idInexistente_debeLanzarExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> categoriaService.actualizarCategoria(99L, categoriaDTO));

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Eliminar categoría existente debe ejecutar la eliminación")
    void eliminarCategoria_idExistente_debeEliminarCorrectamente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.eliminarCategoria(1L);

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar categoría inexistente debe lanzar excepción y no eliminar")
    void eliminarCategoria_idInexistente_debeLanzarExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> categoriaService.eliminarCategoria(99L));

        verify(categoriaRepository, never()).deleteById(anyLong());
    }
}