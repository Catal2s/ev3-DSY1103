package msEjemplares.msEjemplares.ejemplares.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EjemplarDTO {

    @NotBlank(message = "El código no puede estar vacío")
    private String codigo;

    @NotNull(message = "El id del libro no puede ser nulo")
    private Long libroId;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    private String ubicacion;
}