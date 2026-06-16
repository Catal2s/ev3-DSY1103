package msLibros.msLibros.libros.controller;

import msLibros.msLibros.libros.dto.LibroDTO;
import msLibros.msLibros.libros.dto.LibroResponseDTO;
import msLibros.msLibros.libros.model.Libro;
import msLibros.msLibros.libros.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);
    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> listarLibros() {
        logger.info("GET /api/libros");
        return ResponseEntity.ok(libroService.listarLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibro(@PathVariable Long id) {
        logger.info("GET /api/libros/{}", id);
        return ResponseEntity.ok(libroService.obtenerLibroPorId(id));
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO dto) {
        logger.info("POST /api/libros");
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.crearLibro(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroDTO dto) {
        logger.info("PUT /api/libros/{}", id);
        return ResponseEntity.ok(libroService.actualizarLibro(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        logger.info("DELETE /api/libros/{}", id);
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}