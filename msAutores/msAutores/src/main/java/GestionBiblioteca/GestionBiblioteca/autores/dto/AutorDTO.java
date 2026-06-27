package GestionBiblioteca.GestionBiblioteca.autores.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AutorDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    private String nacionalidad;

    @Email(message = "El email no es válido")
    private String email;
}