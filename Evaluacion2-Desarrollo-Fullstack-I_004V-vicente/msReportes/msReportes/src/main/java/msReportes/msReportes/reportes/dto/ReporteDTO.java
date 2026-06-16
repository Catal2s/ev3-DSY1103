package msReportes.msReportes.reportes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReporteDTO {

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    private String descripcion;
    private String fechaGeneracion;
    private String estado;
}