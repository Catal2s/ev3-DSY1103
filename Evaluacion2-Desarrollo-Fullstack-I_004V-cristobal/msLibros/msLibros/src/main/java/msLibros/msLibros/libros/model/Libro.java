package msLibros.msLibros.libros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El ISBN no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "El id del autor no puede ser nulo")
    @Column(nullable = false)
    private Long autorId;

    @NotNull(message = "El id de la categoría no puede ser nulo")
    @Column(nullable = false)
    private Long categoriaId;

    @NotNull(message = "El id de la editorial no puede ser nulo")
    @Column(nullable = false)
    private Long editorialId;

    @Column
    private Integer anioPublicacion;

    @Column
    private String descripcion;
}