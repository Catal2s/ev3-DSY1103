package com.biblioteca.pagos_service.dto;

import java.time.LocalDate;

//DTO pa devolver los datos del pago al front

public class PagoResponseDTO {

    private Long id;
    private Long multaId;
    private Long socioId;
    private Double monto;
    private LocalDate fechaPago;
    private boolean pagado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMultaId() { return multaId; }
    public void setMultaId(Long multaId) { this.multaId = multaId; }

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
}
