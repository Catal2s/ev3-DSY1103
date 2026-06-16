package msEditoriales.msEditoriales.editoriales.service;

import msEditoriales.msEditoriales.editoriales.dto.EditorialDTO;
import msEditoriales.msEditoriales.editoriales.model.Editorial;
import msEditoriales.msEditoriales.editoriales.repository.EditorialRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorialService {

    private static final Logger logger = LoggerFactory.getLogger(EditorialService.class);
    private final EditorialRepository editorialRepository;

    public List<Editorial> listarEditoriales() {
        logger.info("Listando todas las editoriales");
        return editorialRepository.findAll();
    }

    public Editorial obtenerEditorialPorId(Long id) {
        logger.info("Buscando editorial con id: {}", id);
        return editorialRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Editorial con id {} no encontrada", id);
                    return new RuntimeException("Editorial no encontrada con id: " + id);
                });
    }

    public Editorial crearEditorial(EditorialDTO dto) {
        logger.info("Creando nueva editorial: {}", dto.getNombre());
        Editorial editorial = new Editorial();
        editorial.setNombre(dto.getNombre());
        editorial.setPais(dto.getPais());
        editorial.setSitioWeb(dto.getSitioWeb());
        return editorialRepository.save(editorial);
    }

    public Editorial actualizarEditorial(Long id, EditorialDTO dto) {
        logger.info("Actualizando editorial con id: {}", id);
        Editorial editorial = obtenerEditorialPorId(id);
        editorial.setNombre(dto.getNombre());
        editorial.setPais(dto.getPais());
        editorial.setSitioWeb(dto.getSitioWeb());
        return editorialRepository.save(editorial);
    }

    public void eliminarEditorial(Long id) {
        logger.info("Eliminando editorial con id: {}", id);
        editorialRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Editorial con id {} no encontrada para eliminar", id);
                    return new RuntimeException("Editorial no encontrada con id: " + id);
                });
        editorialRepository.deleteById(id);
    }
}