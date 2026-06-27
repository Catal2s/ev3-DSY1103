package msCategoria.msCategoria.categorias.service;

import msCategoria.msCategoria.categorias.dto.CategoriaDTO;
import msCategoria.msCategoria.categorias.model.Categoria;
import msCategoria.msCategoria.categorias.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        logger.info("Listando todas las categorias");
        return categoriaRepository.findAll();
    }

    public Categoria obtenerCategoriaPorId(Long id) {
        logger.info("Buscando categoria con id: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Categoria con id {} no encontrada", id);
                    return new RuntimeException("Categoria no encontrada con id: " + id);
                });
    }

    public Categoria crearCategoria(CategoriaDTO dto) {
        logger.info("Creando nueva categoria: {}", dto.getNombre());
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, CategoriaDTO dto) {
        logger.info("Actualizando categoria con id: {}", id);
        Categoria categoria = obtenerCategoriaPorId(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        logger.info("Eliminando categoria con id: {}", id);
        categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Categoria con id {} no encontrada para eliminar", id);
                    return new RuntimeException("Categoria no encontrada con id: " + id);
                });
        categoriaRepository.deleteById(id);
    }
}