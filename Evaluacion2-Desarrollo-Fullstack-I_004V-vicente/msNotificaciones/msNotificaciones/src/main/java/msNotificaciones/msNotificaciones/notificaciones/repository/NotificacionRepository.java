package msNotificaciones.msNotificaciones.notificaciones.repository;

import msNotificaciones.msNotificaciones.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByEstado(String estado);
    List<Notificacion> findBySocioId(Long socioId);
}
