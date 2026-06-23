package msNotificaciones.msNotificaciones.notificaciones.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entidad, Long id) {
        super(entidad + " no encontrada con id: " + id);
    }
}
