package msNotificaciones.msNotificaciones.notificaciones.service;

import msNotificaciones.msNotificaciones.notificaciones.dto.NotificacionDTO;
import msNotificaciones.msNotificaciones.notificaciones.exception.ResourceNotFoundException;
import msNotificaciones.msNotificaciones.notificaciones.model.Notificacion;
import msNotificaciones.msNotificaciones.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository notificacionRepository;

    public List<Notificacion> listarNotificaciones() {
        logger.info("Listando todas las notificaciones");
        return notificacionRepository.findAll();
    }

    public List<Notificacion> listarPorEstado(String estado) {
        logger.info("Listando notificaciones con estado: {}", estado);
        return notificacionRepository.findByEstado(estado);
    }

    public List<Notificacion> listarPorSocio(Long socioId) {
        logger.info("Listando notificaciones del socio id: {}", socioId);
        return notificacionRepository.findBySocioId(socioId);
    }

    public Notificacion obtenerNotificacionPorId(Long id) {
        logger.info("Buscando notificacion con id: {}", id);
        // Corregido: usa ResourceNotFoundException en lugar de RuntimeException genérico
        return notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notificacion con id {} no encontrada", id);
                    return new ResourceNotFoundException("Notificacion", id);
                });
    }

    public Notificacion crearNotificacion(NotificacionDTO dto) {
        logger.info("Creando nueva notificacion para: {}", dto.getDestinatario());
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setTipo(dto.getTipo());
        // Estado: usa el del DTO si viene, sino deja que @PrePersist asigne "PENDIENTE"
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            notificacion.setEstado(dto.getEstado());
        }
        notificacion.setSocioId(dto.getSocioId());
        // fechaCreacion: usa la del DTO si viene, sino @PrePersist la genera automáticamente
        if (dto.getFechaCreacion() != null) {
            notificacion.setFechaCreacion(dto.getFechaCreacion());
        }
        return notificacionRepository.save(notificacion);
    }

    public Notificacion actualizarNotificacion(Long id, NotificacionDTO dto) {
        logger.info("Actualizando notificacion con id: {}", id);
        Notificacion notificacion = obtenerNotificacionPorId(id);
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setTipo(dto.getTipo());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            notificacion.setEstado(dto.getEstado());
        }
        notificacion.setSocioId(dto.getSocioId());
        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        logger.info("Eliminando notificacion con id: {}", id);
        // Corregido: verificación y eliminación en una sola llamada limpia
        Notificacion notificacion = obtenerNotificacionPorId(id);
        notificacionRepository.delete(notificacion);
    }
}
