package com.biblioteca.reservas_service.service;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.repository.ReservaRepository;
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
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void obtenerTodas_ShouldReturnList() {
        when(reservaRepository.findAll()).thenReturn(List.of(new Reserva()));

        List<Reserva> result = reservaService.obtenerTodas();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerTodas_ShouldReturnEmptyList() {
        when(reservaRepository.findAll()).thenReturn(List.of());

        List<Reserva> result = reservaService.obtenerTodas();

        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorId_ShouldReturnReserva() {
        Reserva r = new Reserva();
        r.setId(1L);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));

        Reserva result = reservaService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_ShouldThrow_WhenNotFound() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservaService.obtenerPorId(99L));
    }

    @Test
    void crearReserva_ShouldSave() {
        Reserva r = new Reserva();
        r.setSocioId(1L);
        r.setLibroId(1L);

        when(reservaRepository.save(any())).thenAnswer(invocation -> {
            Reserva saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Reserva result = reservaService.crearReserva(r);

        assertNotNull(result.getId());
    }

    @Test
    void cancelarReserva_ShouldChangeEstado() {
        Reserva r = new Reserva();
        r.setId(1L);
        r.setEstado("PENDIENTE");
        r.setActivo(true);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservaRepository.save(any())).thenReturn(r);

        Reserva result = reservaService.cancelarReserva(1L);

        assertEquals("CANCELADA", result.getEstado());
        assertFalse(result.isActivo());
    }

    @Test
    void cancelarReserva_ShouldThrow_WhenNotFound() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservaService.cancelarReserva(99L));
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() {
        Reserva r = new Reserva();
        r.setSocioId(1L);
        when(reservaRepository.findBySocioId(1L)).thenReturn(List.of(r));

        List<Reserva> result = reservaService.obtenerPorSocio(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorSocio_ShouldReturnEmptyList() {
        when(reservaRepository.findBySocioId(1L)).thenReturn(List.of());

        List<Reserva> result = reservaService.obtenerPorSocio(1L);

        assertTrue(result.isEmpty());
    }
}
