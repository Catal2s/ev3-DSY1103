package msEjemplares.msEjemplares.ejemplares.service;

import msEjemplares.msEjemplares.ejemplares.dto.EjemplarDTO;
import msEjemplares.msEjemplares.ejemplares.model.Ejemplar;
import msEjemplares.msEjemplares.ejemplares.repository.EjemplarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EjemplarService {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarService.class);
    private final EjemplarRepository ejemplarRepository;

    public List<Ejemplar> listarEjemplares() {
        logger.info("Listando todos los ejemplares");
        return ejemplarRepository.findAll();
    }

    public List<Ejemplar> listarPorLibro(Long libroId) {
        logger.info("Listando ejemplares del libro id: {}", libroId);
        return ejemplarRepository.findByLibroId(libroId);
    }

    public Ejemplar obtenerEjemplarPorId(Long id) {
        logger.info("Buscando ejemplar con id: {}", id);
        return ejemplarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ejemplar con id {} no encontrado", id);
                    return new RuntimeException("Ejemplar no encontrado con id: " + id);
                });
    }

    public Ejemplar crearEjemplar(EjemplarDTO dto) {
        logger.info("Creando nuevo ejemplar con código: {}", dto.getCodigo());
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setCodigo(dto.getCodigo());
        ejemplar.setLibroId(dto.getLibroId());
        ejemplar.setEstado(dto.getEstado());
        ejemplar.setUbicacion(dto.getUbicacion());
        return ejemplarRepository.save(ejemplar);
    }

    public Ejemplar actualizarEjemplar(Long id, EjemplarDTO dto) {
        logger.info("Actualizando ejemplar con id: {}", id);
        Ejemplar ejemplar = obtenerEjemplarPorId(id);
        ejemplar.setCodigo(dto.getCodigo());
        ejemplar.setLibroId(dto.getLibroId());
        ejemplar.setEstado(dto.getEstado());
        ejemplar.setUbicacion(dto.getUbicacion());
        return ejemplarRepository.save(ejemplar);
    }

    public void eliminarEjemplar(Long id) {
        logger.info("Eliminando ejemplar con id: {}", id);
        ejemplarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ejemplar con id {} no encontrado para eliminar", id);
                    return new RuntimeException("Ejemplar no encontrado con id: " + id);
                });
        ejemplarRepository.deleteById(id);
    }
}