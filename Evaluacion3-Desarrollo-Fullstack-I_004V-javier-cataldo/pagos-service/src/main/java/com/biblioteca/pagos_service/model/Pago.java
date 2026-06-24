package com.biblioteca.pagos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidad que representa un pago realizado por un socio.
 * Los pagos estan asociados a una multa y se registran con fecha y monto.
 * La tabla en MySQL se llama "pagos".
 */
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la multa es obligatorio")
    private Long multaId;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El monto del pago es obligatorio")
    private Double monto;

    private LocalDate fechaPago = LocalDate.now();

    private boolean pagado = true;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMultaId() {
        return multaId;
    }

    public void setMultaId(Long multaId) {
        this.multaId = multaId;
    }

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
}
