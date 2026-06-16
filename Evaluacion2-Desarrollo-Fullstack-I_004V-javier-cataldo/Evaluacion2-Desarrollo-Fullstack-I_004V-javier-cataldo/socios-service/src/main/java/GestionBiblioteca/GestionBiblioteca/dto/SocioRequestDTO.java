package GestionBiblioteca.GestionBiblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//DTO pa recibir datos del socio cuando lo crean o actualizan
//Esto separa la entidad de lo que llega por HTTP

public class SocioRequestDTO {

    @NotBlank(message = "El nombre del socio es obligatorio")
    private String nombre;

    @NotBlank(message = "Debe ingresar un RUT")
    private String rut;

    @NotBlank(message = "Debe ingresar un email")
    @Email(message = "El email no es valido")
    private String email;

    @NotBlank(message = "Debe ingresar un telefono")
    private String telefono;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
