package msLibros.msLibros.libros.service;

import msLibros.msLibros.libros.client.AutorClient;
import msLibros.msLibros.libros.client.CategoriaClient;
import msLibros.msLibros.libros.client.EditorialClient;
import msLibros.msLibros.libros.dto.LibroDTO;
import msLibros.msLibros.libros.dto.LibroResponseDTO;
import msLibros.msLibros.libros.model.Libro;
import msLibros.msLibros.libros.repository.LibroRepository;
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
@DisplayName("Pruebas unitarias de LibroService")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private AutorClient autorClient;

    @Mock
    private CategoriaClient categoriaClient;

    @Mock
    private EditorialClient editorialClient;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;
    private LibroDTO libroDTO;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Cien años de soledad");
        libro.setIsbn("978-0307474728");
        libro.setAutorId(5L);
        libro.setCategoriaId(2L);
        libro.setEditorialId(3L);
        libro.setAnioPublicacion(1967);
        libro.setDescripcion("Una de las obras más importantes del realismo mágico");

        libroDTO = new LibroDTO();
        libroDTO.setTitulo("Cien años de soledad");
        libroDTO.setIsbn("978-0307474728");
        libroDTO.setAutorId(5L);
        libroDTO.setCategoriaId(2L);
        libroDTO.setEditorialId(3L);
        libroDTO.setAnioPublicacion(1967);
        libroDTO.setDescripcion("Una de las obras más importantes del realismo mágico");
    }

    @Test
    @DisplayName("Listar libros debe retornar la lista enriquecida con nombres de autor, categoría y editorial")
    void listarLibros_debeRetornarListaEnriquecida() {
        // Given
        when(libroRepository.findAll()).thenReturn(Arrays.asList(libro));
        when(autorClient.obtenerNombreAutor(5L)).thenReturn("Gabriel García Márquez");
        when(categoriaClient.obtenerNombreCategoria(2L)).thenReturn("Ciencia Ficción");
        when(editorialClient.obtenerNombreEditorial(3L)).thenReturn("Penguin Random House");

        // When
        List<LibroResponseDTO> resultado = libroService.listarLibros();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Gabriel García Márquez", resultado.get(0).getNombreAutor());
        assertEquals("Ciencia Ficción", resultado.get(0).getNombreCategoria());
        assertEquals("Penguin Random House", resultado.get(0).getNombreEditorial());
        verify(libroRepository, times(1)).findAll();
        verify(autorClient, times(1)).obtenerNombreAutor(5L);
    }

    @Test
    @DisplayName("Obtener libro por ID existente debe retornar el libro enriquecido")
    void obtenerLibroPorId_idExistente_debeRetornarLibroEnriquecido() {
        // Given
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.obtenerNombreAutor(5L)).thenReturn("Gabriel García Márquez");
        when(categoriaClient.obtenerNombreCategoria(2L)).thenReturn("Ciencia Ficción");
        when(editorialClient.obtenerNombreEditorial(3L)).thenReturn("Penguin Random House");

        // When
        LibroResponseDTO resultado = libroService.obtenerLibroPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());
        assertEquals("Gabriel García Márquez", resultado.getNombreAutor());
        assertEquals("Ciencia Ficción", resultado.getNombreCategoria());
        assertEquals("Penguin Random House", resultado.getNombreEditorial());
        verify(libroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener libro por ID inexistente debe lanzar excepción y no llamar a los clients")
    void obtenerLibroPorId_idInexistente_debeLanzarExcepcion() {
        // Given
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> libroService.obtenerLibroPorId(99L));

        assertTrue(exception.getMessage().contains("Libro no encontrado"));
        verify(libroRepository, times(1)).findById(99L);
        verify(autorClient, never()).obtenerNombreAutor(anyLong());
        verify(categoriaClient, never()).obtenerNombreCategoria(anyLong());
        verify(editorialClient, never()).obtenerNombreEditorial(anyLong());
    }

    @Test
    @DisplayName("Crear libro debe guardar y retornar el libro creado")
    void crearLibro_debeGuardarYRetornarLibro() {
        // Given
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        // When
        Libro resultado = libroService.crearLibro(libroDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());
        assertEquals("978-0307474728", resultado.getIsbn());
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    @DisplayName("Actualizar libro existente debe modificar y retornar el libro")
    void actualizarLibro_idExistente_debeActualizarYRetornarLibro() {
        // Given
        LibroDTO dtoActualizado = new LibroDTO();
        dtoActualizado.setTitulo("Cien años de soledad - Edición especial");
        dtoActualizado.setIsbn("978-0307474728");
        dtoActualizado.setAutorId(5L);
        dtoActualizado.setCategoriaId(2L);
        dtoActualizado.setEditorialId(3L);
        dtoActualizado.setAnioPublicacion(2017);
        dtoActualizado.setDescripcion("Edición conmemorativa");

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        // When
        Libro resultado = libroService.actualizarLibro(1L, dtoActualizado);

        // Then
        assertNotNull(resultado);
        verify(libroRepository, times(1)).findById(1L);
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    @DisplayName("Actualizar libro inexistente debe lanzar excepción")
    void actualizarLibro_idInexistente_debeLanzarExcepcion() {
        // Given
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> libroService.actualizarLibro(99L, libroDTO));

        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("Eliminar libro existente debe ejecutar la eliminación")
    void eliminarLibro_idExistente_debeEliminarCorrectamente() {
        // Given
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        doNothing().when(libroRepository).deleteById(1L);

        // When
        libroService.eliminarLibro(1L);

        // Then
        verify(libroRepository, times(1)).findById(1L);
        verify(libroRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar libro inexistente debe lanzar excepción y no eliminar")
    void eliminarLibro_idInexistente_debeLanzarExcepcion() {
        // Given
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> libroService.eliminarLibro(99L));

        verify(libroRepository, never()).deleteById(anyLong());
    }
}