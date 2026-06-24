package com.biblioteca.prestamos_service.controller;

import com.biblioteca.prestamos_service.dto.PrestamoRequestDTO;
import com.biblioteca.prestamos_service.dto.PrestamoResponseDTO;
import com.biblioteca.prestamos_service.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PrestamoService prestamoService;

    @Test
    void obtenerTodos_ShouldReturnList() throws Exception {
        when(prestamoService.obtenerTodos()).thenReturn(List.of(new PrestamoResponseDTO()));

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.prestamoResponseDTOList.length()").value(1));
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList() throws Exception {
        when(prestamoService.obtenerTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void obtenerPorId_ShouldReturnPrestamo() throws Exception {
        PrestamoResponseDTO dto = new PrestamoResponseDTO();
        dto.setId(1L);
        when(prestamoService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/prestamos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void obtenerPorId_ShouldReturn404_WhenNotFound() throws Exception {
        when(prestamoService.obtenerPorId(99L)).thenThrow(new RuntimeException("Prestamo no encontrado"));

        mockMvc.perform(get("/api/prestamos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorSocio_ShouldReturnList() throws Exception {
        when(prestamoService.obtenerPorSocio(1L)).thenReturn(List.of(new PrestamoResponseDTO()));

        mockMvc.perform(get("/api/prestamos/socio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.prestamoResponseDTOList.length()").value(1));
    }

    @Test
    void crearPrestamo_ShouldReturnCreated() throws Exception {
        PrestamoRequestDTO request = new PrestamoRequestDTO();
        request.setSocioId(1L);
        request.setLibroId(1L);
        request.setFechaDevolucion(LocalDate.now().plusDays(7));

        PrestamoResponseDTO response = new PrestamoResponseDTO();
        response.setId(1L);
        when(prestamoService.crearPrestamo(any())).thenReturn(response);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void crearPrestamo_ShouldReturn400_WhenInvalid() throws Exception {
        PrestamoRequestDTO request = new PrestamoRequestDTO();

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devolverPrestamo_ShouldReturnUpdated() throws Exception {
        PrestamoResponseDTO dto = new PrestamoResponseDTO();
        dto.setId(1L);
        dto.setActivo(false);
        when(prestamoService.devolverPrestamo(1L)).thenReturn(dto);

        mockMvc.perform(put("/api/prestamos/1/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void devolverPrestamo_ShouldReturn404_WhenNotFound() throws Exception {
        when(prestamoService.devolverPrestamo(99L)).thenThrow(new RuntimeException("Prestamo no encontrado"));

        mockMvc.perform(put("/api/prestamos/99/devolver"))
                .andExpect(status().isNotFound());
    }
}
