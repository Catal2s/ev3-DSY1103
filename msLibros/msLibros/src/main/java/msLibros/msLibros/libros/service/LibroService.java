package msLibros.msLibros.libros.service;

import msLibros.msLibros.libros.client.AutorClient;
import msLibros.msLibros.libros.client.CategoriaClient;
import msLibros.msLibros.libros.client.EditorialClient;
import msLibros.msLibros.libros.dto.LibroDTO;
import msLibros.msLibros.libros.dto.LibroResponseDTO;
import msLibros.msLibros.libros.model.Libro;
import msLibros.msLibros.libros.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroService {

    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);
    private final LibroRepository libroRepository;
    private final AutorClient autorClient;
    private final CategoriaClient categoriaClient;
    private final EditorialClient editorialClient;

    public List<LibroResponseDTO> listarLibros() {
        logger.info("Listando todos los libros");
        return libroRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public LibroResponseDTO obtenerLibroPorId(Long id) {
        logger.info("Buscando libro con id: {}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Libro con id {} no encontrado", id);
                    return new RuntimeException("Libro no encontrado con id: " + id);
                });
        return convertirAResponse(libro);
    }

    public Libro crearLibro(LibroDTO dto) {
        logger.info("Creando nuevo libro: {}", dto.getTitulo());
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setIsbn(dto.getIsbn());
        libro.setAutorId(dto.getAutorId());
        libro.setCategoriaId(dto.getCategoriaId());
        libro.setEditorialId(dto.getEditorialId());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setDescripcion(dto.getDescripcion());
        return libroRepository.save(libro);
    }

    public Libro actualizarLibro(Long id, LibroDTO dto) {
        logger.info("Actualizando libro con id: {}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        libro.setTitulo(dto.getTitulo());
        libro.setIsbn(dto.getIsbn());
        libro.setAutorId(dto.getAutorId());
        libro.setCategoriaId(dto.getCategoriaId());
        libro.setEditorialId(dto.getEditorialId());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setDescripcion(dto.getDescripcion());
        return libroRepository.save(libro);
    }

    public void eliminarLibro(Long id) {
        logger.info("Eliminando libro con id: {}", id);
        libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        libroRepository.deleteById(id);
    }

    private LibroResponseDTO convertirAResponse(Libro libro) {
        LibroResponseDTO response = new LibroResponseDTO();
        response.setId(libro.getId());
        response.setTitulo(libro.getTitulo());
        response.setIsbn(libro.getIsbn());
        response.setAutorId(libro.getAutorId());
        response.setNombreAutor(autorClient.obtenerNombreAutor(libro.getAutorId()));
        response.setCategoriaId(libro.getCategoriaId());
        response.setNombreCategoria(categoriaClient.obtenerNombreCategoria(libro.getCategoriaId()));
        response.setEditorialId(libro.getEditorialId());
        response.setNombreEditorial(editorialClient.obtenerNombreEditorial(libro.getEditorialId()));
        response.setAnioPublicacion(libro.getAnioPublicacion());
        response.setDescripcion(libro.getDescripcion());
        return response;
    }
}