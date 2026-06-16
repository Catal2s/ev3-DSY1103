package msNotificaciones.msNotificaciones.notificaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Column(nullable = false)
    private String mensaje;

    @NotBlank(message = "El destinatario no puede estar vacío")
    @Column(nullable = false)
    private String destinatario;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El id del socio no puede ser nulo")
    @Column(nullable = false)
    private Long socioId;

    @Column
    private String fechaCreacion;
}