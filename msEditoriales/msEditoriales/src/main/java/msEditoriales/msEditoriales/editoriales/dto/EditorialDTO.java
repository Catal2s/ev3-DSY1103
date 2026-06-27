package msEditoriales.msEditoriales.editoriales.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditorialDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private String pais;
    private String sitioWeb;
}