package com.biblioteca.pagos_service.dto;

import jakarta.validation.constraints.NotNull;

//DTO pa recibir los datos del pago desde el front

public class PagoRequestDTO {

    @NotNull(message = "El ID de la multa es obligatorio")
    private Long multaId;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    public Long getMultaId() { return multaId; }
    public void setMultaId(Long multaId) { this.multaId = multaId; }

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
