package msProveedores.msProveedores.proveedores.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El RUT no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String rut;

    @Email(message = "El email no es válido")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Column(nullable = false)
    private String telefono;

    @Column
    private String direccion;

    @Column(nullable = false)
    private boolean activo = true;
}