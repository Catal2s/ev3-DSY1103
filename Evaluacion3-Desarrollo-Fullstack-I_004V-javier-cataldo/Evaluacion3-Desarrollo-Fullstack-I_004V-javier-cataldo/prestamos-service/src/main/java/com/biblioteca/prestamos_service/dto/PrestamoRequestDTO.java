package com.biblioteca.prestamos_service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

//DTO pa cuando crean un prestamo desde el front

public class PrestamoRequestDTO {

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    @NotNull(message = "La fecha de devolucion es obligatoria")
    private LocalDate fechaDevolucion;

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
}
