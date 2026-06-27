package GestionBiblioteca.GestionBiblioteca.autores.controller;

import GestionBiblioteca.GestionBiblioteca.autores.dto.AutorDTO;
import GestionBiblioteca.GestionBiblioteca.autores.model.Autor;
import GestionBiblioteca.GestionBiblioteca.autores.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Gestión de autores del sistema de bibliotecas digitales")
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class);
    private final AutorService autorService;

    @Operation(
            summary = "Listar todos los autores",
            description = "Retorna la lista completa de autores registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"nombre\":\"Gabriel García Márquez\",\"nacionalidad\":\"Colombiana\"}]")))
    })
    @GetMapping
    public ResponseEntity<List<Autor>> listarAutores() {
        logger.info("GET /api/autores");
        return ResponseEntity.ok(autorService.listarAutores());
    }

    @Operation(
            summary = "Obtener un autor por ID",
            description = "Retorna los datos de un autor específico según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Gabriel García Márquez\",\"nacionalidad\":\"Colombiana\"}"))),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerAutor(
            @Parameter(description = "ID del autor a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /api/autores/{}", id);
        return ResponseEntity.ok(autorService.obtenerAutorPorId(id));
    }

    @Operation(
            summary = "Crear un nuevo autor",
            description = "Registra un nuevo autor en el sistema, validando los datos obligatorios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":5,\"nombre\":\"Isabel Allende\",\"nacionalidad\":\"Chilena\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Autor> crearAutor(@Valid @RequestBody AutorDTO dto) {
        logger.info("POST /api/autores");
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.crearAutor(dto));
    }

    @Operation(
            summary = "Actualizar un autor existente",
            description = "Modifica los datos de un autor según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Gabriel García Márquez\",\"nacionalidad\":\"Colombiana\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizarAutor(
            @Parameter(description = "ID del autor a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AutorDTO dto) {
        logger.info("PUT /api/autores/{}", id);
        return ResponseEntity.ok(autorService.actualizarAutor(id, dto));
    }

    @Operation(
            summary = "Eliminar un autor",
            description = "Elimina un autor del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(
            @Parameter(description = "ID del autor a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /api/autores/{}", id);
        autorService.eliminarAutor(id);
        return ResponseEntity.noContent().build();
    }
}