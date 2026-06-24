package com.biblioteca.reservas_service.controller;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservaService reservaService;

    @Test
    void obtenerTodas_ShouldReturnList() throws Exception {
        when(reservaService.obtenerTodas()).thenReturn(List.of(new Reserva()));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservaList.length()").value(1));
    }

    @Test
    void obtenerTodas_ShouldReturnEmptyList() throws Exception {
        when(reservaService.obtenerTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void obtenerPorId_ShouldReturnReserva() throws Exception {
        Reserva r = new Reserva();
        r.setId(1L);
        when(reservaService.obtenerPorId(1L)).thenReturn(r);

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void obtenerPorId_ShouldReturn404_WhenNotFound() throws Exception {
        when(reservaService.obtenerPorId(99L)).thenThrow(new RuntimeException("Reserva no encontrada"));

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() throws Exception {
        when(reservaService.obtenerPorSocio(1L)).thenReturn(List.of(new Reserva()));

        mockMvc.perform(get("/api/reservas/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservaList.length()").value(1));
    }

    @Test
    void obtenerPorSocio_ShouldReturnEmptyList() throws Exception {
        when(reservaService.obtenerPorSocio(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/reservas/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void crearReserva_ShouldReturnCreated() throws Exception {
        Reserva request = new Reserva();
        request.setSocioId(1L);
        request.setLibroId(1L);

        Reserva response = new Reserva();
        response.setId(1L);
        when(reservaService.crearReserva(any())).thenReturn(response);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void crearReserva_ShouldReturn400_WhenInvalid() throws Exception {
        Reserva request = new Reserva();

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelarReserva_ShouldReturnCancelled() throws Exception {
        Reserva r = new Reserva();
        r.setId(1L);
        r.setEstado("CANCELADA");
        when(reservaService.cancelarReserva(1L)).thenReturn(r);

        mockMvc.perform(put("/api/reservas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void cancelarReserva_ShouldReturn404_WhenNotFound() throws Exception {
        when(reservaService.cancelarReserva(99L)).thenThrow(new RuntimeException("Reserva no encontrada"));

        mockMvc.perform(put("/api/reservas/99/cancelar"))
                .andExpect(status().isNotFound());
    }
}
