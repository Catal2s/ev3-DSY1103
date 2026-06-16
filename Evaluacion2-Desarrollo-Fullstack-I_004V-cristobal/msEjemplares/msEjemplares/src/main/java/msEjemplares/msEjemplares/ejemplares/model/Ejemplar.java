package msEjemplares.msEjemplares.ejemplares.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "ejemplares")
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotNull(message = "El id del libro no puede ser nulo")
    @Column(nullable = false)
    private Long libroId;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false)
    private String estado; // disponible, prestado, dañado

    @Column
    private String ubicacion;
}