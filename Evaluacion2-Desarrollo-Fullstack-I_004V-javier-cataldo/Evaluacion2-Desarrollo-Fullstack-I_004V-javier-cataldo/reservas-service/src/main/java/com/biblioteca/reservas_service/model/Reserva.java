package com.biblioteca.reservas_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Entidad que representa una reserva de libro en la biblioteca.
 * Una reserva permite a un socio apartar un libro para retirarlo despues.
 * Tabla: reservas
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDate fechaReserva;

    private LocalDate fechaExpiracion;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA

    private boolean activo = true;

    public Reserva() {
        this.fechaReserva = LocalDate.now();
        this.fechaExpiracion = this.fechaReserva.plusDays(3);
        this.estado = "PENDIENTE";
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
