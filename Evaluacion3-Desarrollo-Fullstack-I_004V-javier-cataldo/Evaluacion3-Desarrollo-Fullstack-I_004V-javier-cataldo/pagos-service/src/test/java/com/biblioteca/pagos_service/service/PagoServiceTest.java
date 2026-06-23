package com.biblioteca.pagos_service.service;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.model.Pago;
import com.biblioteca.pagos_service.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void obtenerTodos_ShouldReturnList() {
        Pago pago = new Pago();
        pago.setId(1L);
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<PagoResponseDTO> result = pagoService.obtenerTodos();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList() {
        when(pagoRepository.findAll()).thenReturn(List.of());

        List<PagoResponseDTO> result = pagoService.obtenerTodos();

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorId_ShouldReturnPago() {
        Pago pago = new Pago();
        pago.setId(1L);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoResponseDTO result = pagoService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_ShouldThrow_WhenNotFound() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pagoService.obtenerPorId(99L));
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() {
        Pago pago = new Pago();
        pago.setSocioId(1L);
        when(pagoRepository.findBySocioId(1L)).thenReturn(List.of(pago));

        List<PagoResponseDTO> result = pagoService.obtenerPorSocio(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorSocio_ShouldReturnEmptyList() {
        when(pagoRepository.findBySocioId(1L)).thenReturn(List.of());

        List<PagoResponseDTO> result = pagoService.obtenerPorSocio(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void registrarPago_ShouldThrow_WhenMultaServiceDown() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setMultaId(1L);
        request.setSocioId(1L);
        request.setMonto(5000.0);

        assertThrows(RuntimeException.class, () -> pagoService.registrarPago(request));
    }

    @Test
    void registrarPago_ShouldSave_WhenMultaExists() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setMultaId(1L);
        request.setSocioId(1L);
        request.setMonto(5000.0);

        Pago pago = new Pago();
        pago.setId(1L);
        when(pagoRepository.save(any())).thenReturn(pago);

        assertThrows(RuntimeException.class, () -> pagoService.registrarPago(request));
    }
}
