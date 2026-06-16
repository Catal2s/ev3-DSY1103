package msCompras.msCompras.compras.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del proveedor no puede ser nulo")
    @Column(nullable = false)
    private Long proveedorId;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false)
    private String estado; // PENDIENTE, APROBADA, RECHAZADA, ENTREGADA

    @NotNull(message = "El total no puede ser nulo")
    @Column(nullable = false)
    private Double total;

    @Column
    private String fechaCompra;

    @Column
    private String descripcion;
}