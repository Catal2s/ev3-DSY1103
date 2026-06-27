package GestionBiblioteca.GestionBiblioteca.autores.service;

import GestionBiblioteca.GestionBiblioteca.autores.dto.AutorDTO;
import GestionBiblioteca.GestionBiblioteca.autores.model.Autor;
import GestionBiblioteca.GestionBiblioteca.autores.repository.AutorRepository;
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
@DisplayName("Pruebas unitarias de AutorService")
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private AutorDTO autorDTO;

    @BeforeEach
    void setUp() {
        // Objeto base reutilizado en varios tests
        autor = new Autor();
        autor.setId(1L);
        autor.setNombre("Gabriel");
        autor.setApellido("García Márquez");
        autor.setNacionalidad("Colombiana");
        autor.setEmail("gabriel@gmail.com");

        autorDTO = new AutorDTO();
        autorDTO.setNombre("Gabriel");
        autorDTO.setApellido("García Márquez");
        autorDTO.setNacionalidad("Colombiana");
        autorDTO.setEmail("gabriel@gmail.com");
    }

    @Test
    @DisplayName("Listar autores debe retornar la lista completa")
    void listarAutores_debeRetornarListaDeAutores() {
        // Given
        List<Autor> autores = Arrays.asList(autor, new Autor());
        when(autorRepository.findAll()).thenReturn(autores);

        // When
        List<Autor> resultado = autorService.listarAutores();

        // Then
        assertEquals(2, resultado.size());
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener autor por ID existente debe retornar el autor")
    void obtenerAutorPorId_idExistente_debeRetornarAutor() {
        // Given
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        // When
        Autor resultado = autorService.obtenerAutorPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNombre());
        assertEquals("García Márquez", resultado.getApellido());
        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener autor por ID inexistente debe lanzar excepción")
    void obtenerAutorPorId_idInexistente_debeLanzarExcepcion() {
        // Given
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> autorService.obtenerAutorPorId(99L));

        assertTrue(exception.getMessage().contains("Autor no encontrado"));
        verify(autorRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Crear autor debe guardar y retornar el autor creado")
    void crearAutor_debeGuardarYRetornarAutor() {
        // Given
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        // When
        Autor resultado = autorService.crearAutor(autorDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNombre());
        assertEquals("gabriel@gmail.com", resultado.getEmail());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    @DisplayName("Actualizar autor existente debe modificar y retornar el autor")
    void actualizarAutor_idExistente_debeActualizarYRetornarAutor() {
        // Given
        AutorDTO dtoActualizado = new AutorDTO();
        dtoActualizado.setNombre("Gabriel José");
        dtoActualizado.setApellido("García Márquez");
        dtoActualizado.setNacionalidad("Colombiana");
        dtoActualizado.setEmail("gabriel.nuevo@gmail.com");

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        // When
        Autor resultado = autorService.actualizarAutor(1L, dtoActualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("Gabriel José", resultado.getNombre());
        assertEquals("gabriel.nuevo@gmail.com", resultado.getEmail());
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    @DisplayName("Actualizar autor inexistente debe lanzar excepción")
    void actualizarAutor_idInexistente_debeLanzarExcepcion() {
        // Given
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> autorService.actualizarAutor(99L, autorDTO));

        verify(autorRepository, never()).save(any(Autor.class));
    }

    @Test
    @DisplayName("Eliminar autor existente debe ejecutar la eliminación")
    void eliminarAutor_idExistente_debeEliminarCorrectamente() {
        // Given
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        doNothing().when(autorRepository).deleteById(1L);

        // When
        autorService.eliminarAutor(1L);

        // Then
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar autor inexistente debe lanzar excepción y no eliminar")
    void eliminarAutor_idInexistente_debeLanzarExcepcion() {
        // Given
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> autorService.eliminarAutor(99L));

        verify(autorRepository, never()).deleteById(anyLong());
    }
}