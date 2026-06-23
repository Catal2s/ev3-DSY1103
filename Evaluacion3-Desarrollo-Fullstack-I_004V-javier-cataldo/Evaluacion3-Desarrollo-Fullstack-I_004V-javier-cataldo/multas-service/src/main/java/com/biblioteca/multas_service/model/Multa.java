package com.biblioteca.multas_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Entidad que representa una multa generada por devolucion tardia de un libro.
 * Cuando un prestamo vence y no se devuelve a tiempo, se genera una multa.
 * Tabla: multas
 */
@Entity
@Table(name = "multas")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El ID del prestamo es obligatorio")
    private Long prestamoId;

    @NotNull(message = "El monto de la multa es obligatorio")
    private Double monto;

    private LocalDate fechaGeneracion;

    private LocalDate fechaPago;

    private boolean pagada = false;

    public Multa() {
        this.fechaGeneracion = LocalDate.now();
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

    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }
}
