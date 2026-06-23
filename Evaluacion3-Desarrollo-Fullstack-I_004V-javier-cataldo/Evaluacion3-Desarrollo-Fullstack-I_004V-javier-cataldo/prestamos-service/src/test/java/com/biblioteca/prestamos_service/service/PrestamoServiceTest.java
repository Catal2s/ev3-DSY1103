package com.biblioteca.prestamos_service.service;

import com.biblioteca.prestamos_service.dto.PrestamoRequestDTO;
import com.biblioteca.prestamos_service.dto.PrestamoResponseDTO;
import com.biblioteca.prestamos_service.model.Prestamo;
import com.biblioteca.prestamos_service.repository.PrestamoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PrestamoService prestamoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void obtenerTodos_ShouldReturnList() {
        Prestamo p = new Prestamo();
        p.setId(1L);
        when(prestamoRepository.findAll()).thenReturn(List.of(p));

        List<PrestamoResponseDTO> result = prestamoService.obtenerTodos();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        List<PrestamoResponseDTO> result = prestamoService.obtenerTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorId_ShouldReturnPrestamo() {
        Prestamo p = new Prestamo();
        p.setId(1L);
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(p));

        PrestamoResponseDTO result = prestamoService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_ShouldThrow_WhenNotFound() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> prestamoService.obtenerPorId(99L));
    }

    @Test
    void devolverPrestamo_ShouldDeactivate() {
        Prestamo p = new Prestamo();
        p.setId(1L);
        p.setActivo(true);
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(prestamoRepository.save(any())).thenReturn(p);

        PrestamoResponseDTO result = prestamoService.devolverPrestamo(1L);

        assertFalse(result.isActivo());
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() {
        Prestamo p = new Prestamo();
        p.setSocioId(1L);
        when(prestamoRepository.findBySocioId(1L)).thenReturn(List.of(p));

        List<PrestamoResponseDTO> result = prestamoService.obtenerPorSocio(1L);

        assertEquals(1, result.size());
    }

    @Test
    void crearPrestamo_ShouldThrow_WhenFechaInvalida() {
        PrestamoRequestDTO request = new PrestamoRequestDTO();
        request.setSocioId(1L);
        request.setLibroId(1L);
        request.setFechaDevolucion(LocalDate.now().minusDays(1));

        assertThrows(RuntimeException.class, () -> prestamoService.crearPrestamo(request));
    }

    @Test
    void crearPrestamo_ShouldThrow_WhenSocioNoActivo() {
        PrestamoRequestDTO request = new PrestamoRequestDTO();
        request.setSocioId(1L);
        request.setLibroId(1L);
        request.setFechaDevolucion(LocalDate.now().plusDays(7));

        assertThrows(RuntimeException.class, () -> prestamoService.crearPrestamo(request));
    }
}
