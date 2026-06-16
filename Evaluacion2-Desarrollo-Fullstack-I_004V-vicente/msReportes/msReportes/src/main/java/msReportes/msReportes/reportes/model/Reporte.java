package msReportes.msReportes.reportes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Column(nullable = false)
    private String tipo; // PRESTAMOS, LIBROS, SOCIOS, COMPRAS

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String titulo;

    @Column
    private String descripcion;

    @Column
    private String fechaGeneracion;

    @Column
    private String estado; // GENERADO, PENDIENTE, ERROR
}