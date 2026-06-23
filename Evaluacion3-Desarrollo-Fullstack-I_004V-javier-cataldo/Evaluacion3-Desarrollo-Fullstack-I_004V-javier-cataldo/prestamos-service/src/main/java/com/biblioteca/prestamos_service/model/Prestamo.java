package com.biblioteca.prestamos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidad que representa un prestamo de libro en la biblioteca.
 * Cuando un socio retira un libro, se crea un prestamo con fecha de devolucion.
 * La tabla en MySQL se llama "prestamos".
 */
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    private LocalDate fechaPrestamo = LocalDate.now();

    @NotNull(message = "La fecha de devolucion es obligatoria")
    private LocalDate fechaDevolucion;

    private boolean activo = true;

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

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
