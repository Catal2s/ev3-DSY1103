package com.biblioteca.pagos_service.controller;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.service.PagoService;
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

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PagoService pagoService;

    @Test
    void obtenerTodos_ShouldReturnList() throws Exception {
        when(pagoService.obtenerTodos()).thenReturn(List.of(new PagoResponseDTO()));

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagoResponseDTOList.length()").value(1));
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList() throws Exception {
        when(pagoService.obtenerTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void obtenerPorId_ShouldReturnPago() throws Exception {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(1L);
        when(pagoService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void obtenerPorId_ShouldReturn404_WhenNotFound() throws Exception {
        when(pagoService.obtenerPorId(99L)).thenThrow(new RuntimeException("Pago no encontrado"));

        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() throws Exception {
        when(pagoService.obtenerPorSocio(1L)).thenReturn(List.of(new PagoResponseDTO()));

        mockMvc.perform(get("/api/pagos/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagoResponseDTOList.length()").value(1));
    }

    @Test
    void obtenerPorSocio_ShouldReturnEmptyList() throws Exception {
        when(pagoService.obtenerPorSocio(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/pagos/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void registrarPago_ShouldReturnCreated() throws Exception {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setMultaId(1L);
        request.setSocioId(1L);
        request.setMonto(5000.0);

        PagoResponseDTO response = new PagoResponseDTO();
        response.setId(1L);
        when(pagoService.registrarPago(any())).thenReturn(response);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void registrarPago_ShouldReturn400_WhenInvalid() throws Exception {
        PagoRequestDTO request = new PagoRequestDTO();

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
