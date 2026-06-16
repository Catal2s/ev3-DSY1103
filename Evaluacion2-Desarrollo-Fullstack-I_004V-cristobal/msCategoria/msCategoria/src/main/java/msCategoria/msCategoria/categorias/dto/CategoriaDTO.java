package msCategoria.msCategoria.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private String descripcion;
}