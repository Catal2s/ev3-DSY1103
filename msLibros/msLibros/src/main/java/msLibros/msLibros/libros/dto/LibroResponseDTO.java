package msLibros.msLibros.libros.dto;

import lombok.Data;

@Data
public class LibroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Long autorId;
    private String nombreAutor;
    private Long categoriaId;
    private String nombreCategoria;
    private Long editorialId;
    private String nombreEditorial;
    private Integer anioPublicacion;
    private String descripcion;
}