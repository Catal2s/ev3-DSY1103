package msEditoriales.msEditoriales.editoriales.service;

import msEditoriales.msEditoriales.editoriales.dto.EditorialDTO;
import msEditoriales.msEditoriales.editoriales.model.Editorial;
import msEditoriales.msEditoriales.editoriales.repository.EditorialRepository;
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
@DisplayName("Pruebas unitarias de EditorialService")
class EditorialServiceTest {

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private EditorialService editorialService;

    private Editorial editorial;
    private EditorialDTO editorialDTO;

    @BeforeEach
    void setUp() {
        editorial = new Editorial();
        editorial.setId(1L);
        editorial.setNombre("Penguin Random House");
        editorial.setPais("Estados Unidos");
        editorial.setSitioWeb("https://www.penguinrandomhouse.com");

        editorialDTO = new EditorialDTO();
        editorialDTO.setNombre("Penguin Random House");
        editorialDTO.setPais("Estados Unidos");
        editorialDTO.setSitioWeb("https://www.penguinrandomhouse.com");
    }

    @Test
    @DisplayName("Listar editoriales debe retornar la lista completa")
    void listarEditoriales_debeRetornarListaDeEditoriales() {
        List<Editorial> editoriales = Arrays.asList(editorial, new Editorial());
        when(editorialRepository.findAll()).thenReturn(editoriales);

        List<Editorial> resultado = editorialService.listarEditoriales();

        assertEquals(2, resultado.size());
        verify(editorialRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener editorial por ID existente debe retornar la editorial")
    void obtenerEditorialPorId_idExistente_debeRetornarEditorial() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));

        Editorial resultado = editorialService.obtenerEditorialPorId(1L);

        assertNotNull(resultado);
        assertEquals("Penguin Random House", resultado.getNombre());
        verify(editorialRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener editorial por ID inexistente debe lanzar excepción")
    void obtenerEditorialPorId_idInexistente_debeLanzarExcepcion() {
        when(editorialRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> editorialService.obtenerEditorialPorId(99L));

        assertTrue(exception.getMessage().contains("Editorial no encontrada"));
        verify(editorialRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Crear editorial debe guardar y retornar la editorial creada")
    void crearEditorial_debeGuardarYRetornarEditorial() {
        when(editorialRepository.save(any(Editorial.class))).thenReturn(editorial);

        Editorial resultado = editorialService.crearEditorial(editorialDTO);

        assertNotNull(resultado);
        assertEquals("Penguin Random House", resultado.getNombre());
        assertEquals("Estados Unidos", resultado.getPais());
        verify(editorialRepository, times(1)).save(any(Editorial.class));
    }

    @Test
    @DisplayName("Actualizar editorial existente debe modificar y retornar la editorial")
    void actualizarEditorial_idExistente_debeActualizarYRetornarEditorial() {
        EditorialDTO dtoActualizado = new EditorialDTO();
        dtoActualizado.setNombre("Penguin Random House LATAM");
        dtoActualizado.setPais("México");
        dtoActualizado.setSitioWeb("https://www.penguinrandomhouse.com.mx");

        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));
        when(editorialRepository.save(any(Editorial.class))).thenReturn(editorial);

        Editorial resultado = editorialService.actualizarEditorial(1L, dtoActualizado);

        assertNotNull(resultado);
        assertEquals("Penguin Random House LATAM", resultado.getNombre());
        assertEquals("México", resultado.getPais());
        verify(editorialRepository, times(1)).findById(1L);
        verify(editorialRepository, times(1)).save(any(Editorial.class));
    }

    @Test
    @DisplayName("Actualizar editorial inexistente debe lanzar excepción")
    void actualizarEditorial_idInexistente_debeLanzarExcepcion() {
        when(editorialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> editorialService.actualizarEditorial(99L, editorialDTO));

        verify(editorialRepository, never()).save(any(Editorial.class));
    }

    @Test
    @DisplayName("Eliminar editorial existente debe ejecutar la eliminación")
    void eliminarEditorial_idExistente_debeEliminarCorrectamente() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));
        doNothing().when(editorialRepository).deleteById(1L);

        editorialService.eliminarEditorial(1L);

        verify(editorialRepository, times(1)).findById(1L);
        verify(editorialRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar editorial inexistente debe lanzar excepción y no eliminar")
    void eliminarEditorial_idInexistente_debeLanzarExcepcion() {
        when(editorialRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> editorialService.eliminarEditorial(99L));

        verify(editorialRepository, never()).deleteById(anyLong());
    }
}