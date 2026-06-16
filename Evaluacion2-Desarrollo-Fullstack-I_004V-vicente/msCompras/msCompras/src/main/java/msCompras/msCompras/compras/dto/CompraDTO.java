package msCompras.msCompras.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompraDTO {

    @NotNull(message = "El id del proveedor no puede ser nulo")
    private Long proveedorId;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "El total no puede ser nulo")
    private Double total;

    private String fechaCompra;
    private String descripcion;
}