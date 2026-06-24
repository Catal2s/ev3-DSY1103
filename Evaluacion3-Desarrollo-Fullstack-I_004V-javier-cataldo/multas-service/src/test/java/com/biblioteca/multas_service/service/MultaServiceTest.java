package com.biblioteca.multas_service.service;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.repository.MultaRepository;
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
class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private MultaService multaService;

    @Test
    void obtenerTodas_ShouldReturnList() {
        when(multaRepository.findAll()).thenReturn(List.of(new Multa()));

        List<Multa> result = multaService.obtenerTodas();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerTodas_ShouldReturnEmptyList() {
        when(multaRepository.findAll()).thenReturn(List.of());

        List<Multa> result = multaService.obtenerTodas();

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorId_ShouldReturnMulta() {
        Multa m = new Multa();
        m.setId(1L);
        when(multaRepository.findById(1L)).thenReturn(Optional.of(m));

        Multa result = multaService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_ShouldThrow_WhenNotFound() {
        when(multaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> multaService.obtenerPorId(99L));
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() {
        Multa m = new Multa();
        m.setSocioId(1L);
        when(multaRepository.findBySocioId(1L)).thenReturn(List.of(m));

        List<Multa> result = multaService.obtenerPorSocio(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorSocio_ShouldReturnEmptyList() {
        when(multaRepository.findBySocioId(1L)).thenReturn(List.of());

        List<Multa> result = multaService.obtenerPorSocio(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPendientes_ShouldReturnUnpaid() {
        Multa m = new Multa();
        m.setPagada(false);
        when(multaRepository.findByPagadaFalse()).thenReturn(List.of(m));

        List<Multa> result = multaService.obtenerPendientes();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isPagada());
    }

    @Test
    void obtenerPendientes_ShouldReturnEmpty_WhenAllPaid() {
        when(multaRepository.findByPagadaFalse()).thenReturn(List.of());

        List<Multa> result = multaService.obtenerPendientes();

        assertTrue(result.isEmpty());
    }

    @Test
    void crearMulta_ShouldThrow_WhenMontoInvalido() {
        Multa m = new Multa();
        m.setMonto(0.0);

        assertThrows(RuntimeException.class, () -> multaService.crearMulta(m));
    }

    @Test
    void crearMulta_ShouldThrow_WhenPrestamoServiceDown() {
        Multa m = new Multa();
        m.setMonto(5000.0);
        m.setPrestamoId(1L);
        m.setSocioId(1L);

        assertThrows(RuntimeException.class, () -> multaService.crearMulta(m));
    }

    @Test
    void pagarMulta_ShouldMarkAsPaid() {
        Multa m = new Multa();
        m.setId(1L);
        m.setPagada(false);
        when(multaRepository.findById(1L)).thenReturn(Optional.of(m));
        when(multaRepository.save(any())).thenReturn(m);

        Multa result = multaService.pagarMulta(1L);

        assertTrue(result.isPagada());
        assertNotNull(result.getFechaPago());
    }

    @Test
    void pagarMulta_ShouldThrow_WhenNotFound() {
        when(multaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> multaService.pagarMulta(99L));
    }
}
