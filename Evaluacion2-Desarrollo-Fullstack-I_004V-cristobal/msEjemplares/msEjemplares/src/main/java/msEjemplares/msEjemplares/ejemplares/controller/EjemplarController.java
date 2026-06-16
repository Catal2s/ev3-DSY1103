package msEjemplares.msEjemplares.ejemplares.controller;

import msEjemplares.msEjemplares.ejemplares.dto.EjemplarDTO;
import msEjemplares.msEjemplares.ejemplares.model.Ejemplar;
import msEjemplares.msEjemplares.ejemplares.service.EjemplarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarController.class);
    private final EjemplarService ejemplarService;

    @GetMapping
    public ResponseEntity<List<Ejemplar>> listarEjemplares() {
        logger.info("GET /api/ejemplares");
        return ResponseEntity.ok(ejemplarService.listarEjemplares());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> obtenerEjemplar(@PathVariable Long id) {
        logger.info("GET /api/ejemplares/{}", id);
        return ResponseEntity.ok(ejemplarService.obtenerEjemplarPorId(id));
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Ejemplar>> listarPorLibro(@PathVariable Long libroId) {
        logger.info("GET /api/ejemplares/libro/{}", libroId);
        return ResponseEntity.ok(ejemplarService.listarPorLibro(libroId));
    }

    @PostMapping
    public ResponseEntity<Ejemplar> crearEjemplar(@Valid @RequestBody EjemplarDTO dto) {
        logger.info("POST /api/ejemplares");
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.crearEjemplar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> actualizarEjemplar(@PathVariable Long id, @Valid @RequestBody EjemplarDTO dto) {
        logger.info("PUT /api/ejemplares/{}", id);
        return ResponseEntity.ok(ejemplarService.actualizarEjemplar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(@PathVariable Long id) {
        logger.info("DELETE /api/ejemplares/{}", id);
        ejemplarService.eliminarEjemplar(id);
        return ResponseEntity.noContent().build();
    }
}