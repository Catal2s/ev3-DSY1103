package GestionBiblioteca.GestionBiblioteca.dto;

import java.time.LocalDateTime;

//DTO pa devolver los errores de forma estructurada
//Asi el frontend siempre recibe el mismo formato cuando hay error

public class ErrorResponseDTO {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
