package msEditoriales.msEditoriales.editoriales.controller;

import msEditoriales.msEditoriales.editoriales.dto.EditorialDTO;
import msEditoriales.msEditoriales.editoriales.model.Editorial;
import msEditoriales.msEditoriales.editoriales.service.EditorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editoriales")
@RequiredArgsConstructor
@Tag(name = "Editoriales", description = "Gestión de editoriales del sistema de bibliotecas digitales")
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class);
    private final EditorialService editorialService;

    @Operation(
            summary = "Listar todas las editoriales",
            description = "Retorna la lista completa de editoriales registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"nombre\":\"Penguin Random House\",\"pais\":\"Estados Unidos\",\"sitioWeb\":\"https://www.penguinrandomhouse.com\"}]")))
    })
    @GetMapping
    public ResponseEntity<List<Editorial>> listarEditoriales() {
        logger.info("GET /api/editoriales");
        return ResponseEntity.ok(editorialService.listarEditoriales());
    }

    @Operation(
            summary = "Obtener una editorial por ID",
            description = "Retorna los datos de una editorial específica según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Editorial encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Penguin Random House\",\"pais\":\"Estados Unidos\",\"sitioWeb\":\"https://www.penguinrandomhouse.com\"}"))),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Editorial> obtenerEditorial(
            @Parameter(description = "ID de la editorial a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.obtenerEditorialPorId(id));
    }

    @Operation(
            summary = "Crear una nueva editorial",
            description = "Registra una nueva editorial en el sistema, validando los datos obligatorios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Editorial creada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":5,\"nombre\":\"Planeta\",\"pais\":\"España\",\"sitioWeb\":\"https://www.planetadelibros.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Editorial> crearEditorial(@Valid @RequestBody EditorialDTO dto) {
        logger.info("POST /api/editoriales");
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.crearEditorial(dto));
    }

    @Operation(
            summary = "Actualizar una editorial existente",
            description = "Modifica los datos de una editorial según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Editorial actualizada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Penguin Random House\",\"pais\":\"Estados Unidos\",\"sitioWeb\":\"https://www.penguinrandomhouse.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizarEditorial(
            @Parameter(description = "ID de la editorial a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EditorialDTO dto) {
        logger.info("PUT /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.actualizarEditorial(id, dto));
    }

    @Operation(
            summary = "Eliminar una editorial",
            description = "Elimina una editorial del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Editorial eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEditorial(
            @Parameter(description = "ID de la editorial a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /api/editoriales/{}", id);
        editorialService.eliminarEditorial(id);
        return ResponseEntity.noContent().build();
    }
}