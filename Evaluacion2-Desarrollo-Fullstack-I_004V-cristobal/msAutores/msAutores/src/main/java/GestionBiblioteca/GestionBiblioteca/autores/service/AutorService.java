package GestionBiblioteca.GestionBiblioteca.autores.service;

import GestionBiblioteca.GestionBiblioteca.autores.dto.AutorDTO;
import GestionBiblioteca.GestionBiblioteca.autores.model.Autor;
import GestionBiblioteca.GestionBiblioteca.autores.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private static final Logger logger = LoggerFactory.getLogger(AutorService.class);
    private final AutorRepository autorRepository;

    public List<Autor> listarAutores() {
        logger.info("Listando todos los autores");
        return autorRepository.findAll();
    }

    public Autor obtenerAutorPorId(Long id) {
        logger.info("Buscando autor con id: {}", id);
        return autorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Autor con id {} no encontrado", id);
                    return new RuntimeException("Autor no encontrado con id: " + id);
                });
    }

    public Autor crearAutor(AutorDTO dto) {
        logger.info("Creando nuevo autor: {} {}", dto.getNombre(), dto.getApellido());
        Autor autor = new Autor();
        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setEmail(dto.getEmail());
        return autorRepository.save(autor);
    }

    public Autor actualizarAutor(Long id, AutorDTO dto) {
        logger.info("Actualizando autor con id: {}", id);
        Autor autor = obtenerAutorPorId(id);
        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setEmail(dto.getEmail());
        return autorRepository.save(autor);
    }

    public void eliminarAutor(Long id) {
        logger.info("Eliminando autor con id: {}", id);
        autorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Autor con id {} no encontrado para eliminar", id);
                    return new RuntimeException("Autor no encontrado con id: " + id);
                });
        autorRepository.deleteById(id);
    }
}