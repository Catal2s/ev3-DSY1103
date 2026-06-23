package GestionBiblioteca.GestionBiblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad que representa un socio de la biblioteca.
 * Cada socio tiene datos personales como nombre, RUT, email y telefono.
 * La tabla en MySQL se llama "socios".
 */
@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del socio es obligatorio.")
    private String nombre;

    @NotBlank(message = "Debe ingresar un RUT.")
    @Column(unique = true)
    private String rut;

    @NotBlank(message = "Debe ingresar un email.")
    @Email(message = "El email ingresado no tiene un formato valido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Debe ingresar un numero de telefono.")
    private String telefono;

    private boolean activo = true;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
