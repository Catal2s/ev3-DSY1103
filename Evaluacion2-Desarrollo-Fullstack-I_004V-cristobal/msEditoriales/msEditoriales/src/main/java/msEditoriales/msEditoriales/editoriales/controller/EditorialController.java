package msEditoriales.msEditoriales.editoriales.controller;

import msEditoriales.msEditoriales.editoriales.dto.EditorialDTO;
import msEditoriales.msEditoriales.editoriales.model.Editorial;
import msEditoriales.msEditoriales.editoriales.service.EditorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editoriales")
@RequiredArgsConstructor
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class);
    private final EditorialService editorialService;

    @GetMapping
    public ResponseEntity<List<Editorial>> listarEditoriales() {
        logger.info("GET /api/editoriales");
        return ResponseEntity.ok(editorialService.listarEditoriales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editorial> obtenerEditorial(@PathVariable Long id) {
        logger.info("GET /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.obtenerEditorialPorId(id));
    }

    @PostMapping
    public ResponseEntity<Editorial> crearEditorial(@Valid @RequestBody EditorialDTO dto) {
        logger.info("POST /api/editoriales");
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.crearEditorial(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizarEditorial(@PathVariable Long id, @Valid @RequestBody EditorialDTO dto) {
        logger.info("PUT /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.actualizarEditorial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEditorial(@PathVariable Long id) {
        logger.info("DELETE /api/editoriales/{}", id);
        editorialService.eliminarEditorial(id);
        return ResponseEntity.noContent().build();
    }
}