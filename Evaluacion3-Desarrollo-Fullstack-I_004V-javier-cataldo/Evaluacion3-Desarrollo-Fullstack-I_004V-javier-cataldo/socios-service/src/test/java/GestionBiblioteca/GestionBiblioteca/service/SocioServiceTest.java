package GestionBiblioteca.GestionBiblioteca.service;

import GestionBiblioteca.GestionBiblioteca.dto.SocioRequestDTO;
import GestionBiblioteca.GestionBiblioteca.dto.SocioResponseDTO;
import GestionBiblioteca.GestionBiblioteca.model.Socio;
import GestionBiblioteca.GestionBiblioteca.repository.SocioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @InjectMocks
    private SocioService socioService;

    @Test
    void obtenerTodos_ShouldReturnList() {
        Socio socio = new Socio();
        socio.setId(1L);
        socio.setNombre("Juan");
        when(socioRepository.findAll()).thenReturn(List.of(socio));

        List<SocioResponseDTO> result = socioService.obtenerTodos();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList_WhenNoSocios() {
        when(socioRepository.findAll()).thenReturn(List.of());

        List<SocioResponseDTO> result = socioService.obtenerTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorId_ShouldReturnSocio() {
        Socio socio = new Socio();
        socio.setId(1L);
        socio.setNombre("Juan");
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));

        SocioResponseDTO result = socioService.obtenerPorId(1L);

        assertEquals("Juan", result.getNombre());
    }

    @Test
    void obtenerPorId_ShouldThrow_WhenNotFound() {
        when(socioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> socioService.obtenerPorId(99L));
    }

    @Test
    void crearSocio_ShouldReturnCreatedSocio() {
        SocioRequestDTO request = new SocioRequestDTO();
        request.setNombre("Juan");
        request.setRut("12.345.678-9");
        request.setEmail("juan@test.com");
        request.setTelefono("912345678");

        when(socioRepository.existsByRut(any())).thenReturn(false);
        when(socioRepository.existsByEmail(any())).thenReturn(false);
        when(socioRepository.save(any())).thenAnswer(invocation -> {
            Socio s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });

        SocioResponseDTO result = socioService.crearSocio(request);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void crearSocio_ShouldThrow_WhenRutExists() {
        SocioRequestDTO request = new SocioRequestDTO();
        request.setRut("12.345.678-9");
        when(socioRepository.existsByRut("12.345.678-9")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> socioService.crearSocio(request));
    }

    @Test
    void actualizarSocio_ShouldUpdateAndReturn() {
        Socio existing = new Socio();
        existing.setId(1L);
        existing.setNombre("Old");

        SocioRequestDTO request = new SocioRequestDTO();
        request.setNombre("New");
        request.setRut("12.345.678-9");
        request.setEmail("new@test.com");
        request.setTelefono("912345678");

        when(socioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(socioRepository.save(any())).thenReturn(existing);

        SocioResponseDTO result = socioService.actualizarSocio(1L, request);

        assertEquals("New", result.getNombre());
    }

    @Test
    void eliminarSocio_ShouldDelete() {
        doNothing().when(socioRepository).deleteById(1L);

        socioService.eliminarSocio(1L);

        verify(socioRepository, times(1)).deleteById(1L);
    }
}
