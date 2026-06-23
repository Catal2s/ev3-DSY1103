package msNotificaciones.msNotificaciones.notificaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

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

    // Estado con valor por defecto garantizado desde la entidad
    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @NotNull(message = "El id del socio no puede ser nulo")
    @Column(nullable = false)
    private Long socioId;

    // Corregido: LocalDateTime en lugar de String
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    // Se asigna automáticamente antes de persistir
    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "PENDIENTE";
        }
    }
}
