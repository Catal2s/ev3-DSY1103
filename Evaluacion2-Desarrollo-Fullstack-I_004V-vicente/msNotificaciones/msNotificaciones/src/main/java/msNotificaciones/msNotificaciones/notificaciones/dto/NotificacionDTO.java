package msNotificaciones.msNotificaciones.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionDTO {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotBlank(message = "El destinatario no puede estar vacío")
    private String destinatario;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    // Estado opcional desde el cliente; la entidad lo asigna si viene nulo
    private String estado;

    @NotNull(message = "El id del socio no puede ser nulo")
    private Long socioId;

    // Corregido: LocalDateTime en lugar de String
    private LocalDateTime fechaCreacion;
}
