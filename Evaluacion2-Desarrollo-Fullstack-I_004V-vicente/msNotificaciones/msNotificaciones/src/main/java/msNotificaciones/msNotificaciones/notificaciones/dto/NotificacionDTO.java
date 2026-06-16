package msNotificaciones.msNotificaciones.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionDTO {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotBlank(message = "El destinatario no puede estar vacío")
    private String destinatario;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    private String estado = "PENDIENTE";

    @NotNull(message = "El id del socio no puede ser nulo")
    private Long socioId;

    private String fechaCreacion;
}