package com.biblioteca.multas_service.controller;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.service.MultaService;
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

@WebMvcTest(MultaController.class)
class MultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MultaService multaService;

    @Test
    void obtenerTodas_ShouldReturnList() throws Exception {
        when(multaService.obtenerTodas()).thenReturn(List.of(new Multa()));

        mockMvc.perform(get("/api/multas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerTodas_ShouldReturnEmptyList() throws Exception {
        when(multaService.obtenerTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/multas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void obtenerPorId_ShouldReturnMulta() throws Exception {
        Multa m = new Multa();
        m.setId(1L);
        when(multaService.obtenerPorId(1L)).thenReturn(m);

        mockMvc.perform(get("/api/multas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerPorId_ShouldReturn404_WhenNotFound() throws Exception {
        when(multaService.obtenerPorId(99L)).thenThrow(new RuntimeException("Multa no encontrada"));

        mockMvc.perform(get("/api/multas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() throws Exception {
        when(multaService.obtenerPorSocio(1L)).thenReturn(List.of(new Multa()));

        mockMvc.perform(get("/api/multas/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerPendientes_ShouldReturnList() throws Exception {
        when(multaService.obtenerPendientes()).thenReturn(List.of(new Multa()));

        mockMvc.perform(get("/api/multas/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerPendientes_ShouldReturnEmptyList() throws Exception {
        when(multaService.obtenerPendientes()).thenReturn(List.of());

        mockMvc.perform(get("/api/multas/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void crearMulta_ShouldReturnCreated() throws Exception {
        Multa request = new Multa();
        request.setSocioId(1L);
        request.setPrestamoId(1L);
        request.setMonto(5000.0);

        Multa response = new Multa();
        response.setId(1L);
        when(multaService.crearMulta(any())).thenReturn(response);

        mockMvc.perform(post("/api/multas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearMulta_ShouldReturn400_WhenInvalid() throws Exception {
        Multa request = new Multa();

        mockMvc.perform(post("/api/multas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pagarMulta_ShouldReturnPaid() throws Exception {
        Multa m = new Multa();
        m.setId(1L);
        m.setPagada(true);
        when(multaService.pagarMulta(1L)).thenReturn(m);

        mockMvc.perform(put("/api/multas/1/pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagada").value(true));
    }

    @Test
    void pagarMulta_ShouldReturn404_WhenNotFound() throws Exception {
        when(multaService.pagarMulta(99L)).thenThrow(new RuntimeException("Multa no encontrada"));

        mockMvc.perform(put("/api/multas/99/pagar"))
                .andExpect(status().isNotFound());
    }
}
