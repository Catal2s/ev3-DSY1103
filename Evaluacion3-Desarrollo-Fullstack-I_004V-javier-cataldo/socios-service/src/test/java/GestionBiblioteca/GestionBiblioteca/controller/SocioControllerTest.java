package GestionBiblioteca.GestionBiblioteca.controller;

import GestionBiblioteca.GestionBiblioteca.dto.SocioRequestDTO;
import GestionBiblioteca.GestionBiblioteca.dto.SocioResponseDTO;
import GestionBiblioteca.GestionBiblioteca.service.SocioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SocioController.class)
class SocioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SocioService socioService;

    @Test
    void obtenerTodos_ShouldReturnList() throws Exception {
        when(socioService.obtenerTodos()).thenReturn(List.of(new SocioResponseDTO()));

        mockMvc.perform(get("/api/socios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerTodos_ShouldReturnEmptyList() throws Exception {
        when(socioService.obtenerTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/socios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void obtenerPorId_ShouldReturnSocio() throws Exception {
        SocioResponseDTO dto = new SocioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Juan");
        when(socioService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/socios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerPorId_ShouldReturn404_WhenNotFound() throws Exception {
        when(socioService.obtenerPorId(99L)).thenThrow(new RuntimeException("Socio no encontrado con id: 99"));

        mockMvc.perform(get("/api/socios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearSocio_ShouldReturnCreatedSocio() throws Exception {
        SocioRequestDTO request = new SocioRequestDTO();
        request.setNombre("Juan");
        request.setRut("12.345.678-9");
        request.setEmail("juan@test.com");
        request.setTelefono("912345678");

        SocioResponseDTO response = new SocioResponseDTO();
        response.setId(1L);
        response.setNombre("Juan");
        when(socioService.crearSocio(any())).thenReturn(response);

        mockMvc.perform(post("/api/socios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearSocio_ShouldReturn400_WhenInvalid() throws Exception {
        SocioRequestDTO request = new SocioRequestDTO();

        mockMvc.perform(post("/api/socios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarSocio_ShouldReturnUpdatedSocio() throws Exception {
        SocioRequestDTO request = new SocioRequestDTO();
        request.setNombre("Juan Updated");
        request.setRut("12.345.678-9");
        request.setEmail("juan@test.com");
        request.setTelefono("912345678");

        SocioResponseDTO response = new SocioResponseDTO();
        response.setId(1L);
        response.setNombre("Juan Updated");
        when(socioService.actualizarSocio(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/socios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Updated"));
    }

    @Test
    void eliminarSocio_ShouldReturn204() throws Exception {
        doNothing().when(socioService).eliminarSocio(1L);

        mockMvc.perform(delete("/api/socios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarSocio_ShouldReturn404_WhenNotFound() throws Exception {
        doNothing().when(socioService).eliminarSocio(99L);

        mockMvc.perform(delete("/api/socios/99"))
                .andExpect(status().isNoContent());
    }
}
