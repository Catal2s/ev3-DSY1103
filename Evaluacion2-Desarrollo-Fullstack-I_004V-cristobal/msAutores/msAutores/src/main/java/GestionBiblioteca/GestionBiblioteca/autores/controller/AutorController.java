package GestionBiblioteca.GestionBiblioteca.autores.controller;

import GestionBiblioteca.GestionBiblioteca.autores.dto.AutorDTO;
import GestionBiblioteca.GestionBiblioteca.autores.model.Autor;
import GestionBiblioteca.GestionBiblioteca.autores.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class);
    private final AutorService autorService;

    @GetMapping
    public ResponseEntity<List<Autor>> listarAutores() {
        logger.info("GET /api/autores");
        return ResponseEntity.ok(autorService.listarAutores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerAutor(@PathVariable Long id) {
        logger.info("GET /api/autores/{}", id);
        return ResponseEntity.ok(autorService.obtenerAutorPorId(id));
    }

    @PostMapping
    public ResponseEntity<Autor> crearAutor(@Valid @RequestBody AutorDTO dto) {
        logger.info("POST /api/autores");
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.crearAutor(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizarAutor(@PathVariable Long id, @Valid @RequestBody AutorDTO dto) {
        logger.info("PUT /api/autores/{}", id);
        return ResponseEntity.ok(autorService.actualizarAutor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) {
        logger.info("DELETE /api/autores/{}", id);
        autorService.eliminarAutor(id);
        return ResponseEntity.noContent().build();
    }
}