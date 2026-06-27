package msLibros.msLibros.libros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibroDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El ISBN no puede estar vacío")
    private String isbn;

    @NotNull(message = "El id del autor no puede ser nulo")
    private Long autorId;

    @NotNull(message = "El id de la categoría no puede ser nulo")
    private Long categoriaId;

    @NotNull(message = "El id de la editorial no puede ser nulo")
    private Long editorialId;

    private Integer anioPublicacion;
    private String descripcion;
}