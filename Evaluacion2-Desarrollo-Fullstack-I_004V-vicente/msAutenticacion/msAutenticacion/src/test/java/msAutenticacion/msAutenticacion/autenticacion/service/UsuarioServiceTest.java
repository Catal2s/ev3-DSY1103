package msAutenticacion.msAutenticacion.autenticacion.service;

import msAutenticacion.msAutenticacion.autenticacion.dto.UsuarioDTO;
import msAutenticacion.msAutenticacion.autenticacion.model.Usuario;
import msAutenticacion.msAutenticacion.autenticacion.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Perez");
        usuario.setEmail("juan@biblioteca.cl");
        usuario.setPassword("pass123");
        usuario.setRol("SOCIO");
        usuario.setActivo(true);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan Perez");
        usuarioDTO.setEmail("juan@biblioteca.cl");
        usuarioDTO.setPassword("pass123");
        usuarioDTO.setRol("SOCIO");
        usuarioDTO.setActivo(true);
    }

    // ─── GET /api/usuarios ───────────────────────────────────────────────────

    @Test
    void listarUsuarios_conUsuariosExistentes_retornaLista() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // When
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void listarUsuarios_sinUsuarios_retornaListaVacia() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    // ─── GET /api/usuarios/{id} ──────────────────────────────────────────────

    @Test
    void obtenerUsuarioPorId_idExistente_retornaUsuario() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = usuarioService.obtenerUsuarioPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerUsuarioPorId_idNoExistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerUsuarioPorId(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(usuarioRepository, times(1)).findById(99L);
    }

    // ─── GET /api/usuarios/email/{email} ─────────────────────────────────────

    @Test
    void obtenerPorEmail_emailExistente_retornaUsuario() {
        // Given
        when(usuarioRepository.findByEmail("juan@biblioteca.cl")).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = usuarioService.obtenerPorEmail("juan@biblioteca.cl");

        // Then
        assertNotNull(resultado);
        assertEquals("juan@biblioteca.cl", resultado.getEmail());
        verify(usuarioRepository, times(1)).findByEmail("juan@biblioteca.cl");
    }

    @Test
    void obtenerPorEmail_emailNoExistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findByEmail("noexiste@correo.cl")).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerPorEmail("noexiste@correo.cl"));
        assertTrue(ex.getMessage().contains("noexiste@correo.cl"));
        verify(usuarioRepository, times(1)).findByEmail("noexiste@correo.cl");
    }

    // ─── GET /api/usuarios/rol/{rol} ─────────────────────────────────────────

    @Test
    void listarPorRol_rolExistente_retornaLista() {
        // Given
        when(usuarioRepository.findByRol("SOCIO")).thenReturn(Arrays.asList(usuario));

        // When
        List<Usuario> resultado = usuarioService.listarPorRol("SOCIO");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("SOCIO", resultado.get(0).getRol());
        verify(usuarioRepository, times(1)).findByRol("SOCIO");
    }

    @Test
    void listarPorRol_rolSinUsuarios_retornaListaVacia() {
        // Given
        when(usuarioRepository.findByRol("ADMIN")).thenReturn(Collections.emptyList());

        // When
        List<Usuario> resultado = usuarioService.listarPorRol("ADMIN");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findByRol("ADMIN");
    }

    // ─── GET /api/usuarios/activos ────────────────────────────────────────────

    @Test
    void listarUsuariosActivos_conActivos_retornaLista() {
        // Given
        when(usuarioRepository.findByActivoTrue()).thenReturn(Arrays.asList(usuario));

        // When
        List<Usuario> resultado = usuarioService.listarUsuariosActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        verify(usuarioRepository, times(1)).findByActivoTrue();
    }

    @Test
    void listarUsuariosActivos_sinActivos_retornaListaVacia() {
        // Given
        when(usuarioRepository.findByActivoTrue()).thenReturn(Collections.emptyList());

        // When
        List<Usuario> resultado = usuarioService.listarUsuariosActivos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findByActivoTrue();
    }

    // ─── POST /api/usuarios ───────────────────────────────────────────────────

    @Test
    void crearUsuario_emailNuevo_retornaUsuarioCreado() {
        // Given
        when(usuarioRepository.existsByEmail("juan@biblioteca.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.crearUsuario(usuarioDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("juan@biblioteca.cl", resultado.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail("juan@biblioteca.cl");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        // Given
        when(usuarioRepository.existsByEmail("juan@biblioteca.cl")).thenReturn(true);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crearUsuario(usuarioDTO));
        assertTrue(ex.getMessage().contains("juan@biblioteca.cl"));
        verify(usuarioRepository, times(1)).existsByEmail("juan@biblioteca.cl");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ─── PUT /api/usuarios/{id} ───────────────────────────────────────────────

    @Test
    void actualizarUsuario_idExistente_retornaUsuarioActualizado() {
        // Given
        usuarioDTO.setNombre("Juan Actualizado");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Usuario resultado = usuarioService.actualizarUsuario(1L, usuarioDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_idNoExistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarUsuario(99L, usuarioDTO));
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ─── DELETE /api/usuarios/{id} ────────────────────────────────────────────

    @Test
    void eliminarUsuario_idExistente_ejecutaSinErrores() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);

        // When
        usuarioService.eliminarUsuario(1L);

        // Then
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarUsuario_idNoExistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class,
                () -> usuarioService.eliminarUsuario(99L));
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
