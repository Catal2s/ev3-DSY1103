package msEjemplares.msEjemplares.ejemplares.controller;

import msEjemplares.msEjemplares.ejemplares.dto.EjemplarDTO;
import msEjemplares.msEjemplares.ejemplares.model.Ejemplar;
import msEjemplares.msEjemplares.ejemplares.service.EjemplarService;
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
@RequestMapping("/api/ejemplares")
@RequiredArgsConstructor
@Tag(name = "Ejemplares", description = "Gestión de ejemplares físicos de libros del sistema de bibliotecas digitales")
public class EjemplarController {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarController.class);
    private final EjemplarService ejemplarService;

    @Operation(
            summary = "Listar todos los ejemplares",
            description = "Retorna la lista completa de ejemplares registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"codigo\":\"EJ-0001\",\"libroId\":3,\"estado\":\"DISPONIBLE\",\"ubicacion\":\"Estante A-12\"}]")))
    })
    @GetMapping
    public ResponseEntity<List<Ejemplar>> listarEjemplares() {
        logger.info("GET /api/ejemplares");
        return ResponseEntity.ok(ejemplarService.listarEjemplares());
    }

    @Operation(
            summary = "Obtener un ejemplar por ID",
            description = "Retorna los datos de un ejemplar específico según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejemplar encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"codigo\":\"EJ-0001\",\"libroId\":3,\"estado\":\"DISPONIBLE\",\"ubicacion\":\"Estante A-12\"}"))),
            @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> obtenerEjemplar(
            @Parameter(description = "ID del ejemplar a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /api/ejemplares/{}", id);
        return ResponseEntity.ok(ejemplarService.obtenerEjemplarPorId(id));
    }

    @Operation(
            summary = "Listar ejemplares por libro",
            description = "Retorna todos los ejemplares físicos asociados a un libro específico, identificado por su libroId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ejemplares del libro obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"codigo\":\"EJ-0001\",\"libroId\":3,\"estado\":\"DISPONIBLE\",\"ubicacion\":\"Estante A-12\"}]")))
    })
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Ejemplar>> listarPorLibro(
            @Parameter(description = "ID del libro al que pertenecen los ejemplares", example = "3")
            @PathVariable Long libroId) {
        logger.info("GET /api/ejemplares/libro/{}", libroId);
        return ResponseEntity.ok(ejemplarService.listarPorLibro(libroId));
    }

    @Operation(
            summary = "Crear un nuevo ejemplar",
            description = "Registra un nuevo ejemplar físico asociado a un libro, validando los datos obligatorios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ejemplar creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":5,\"codigo\":\"EJ-0005\",\"libroId\":3,\"estado\":\"DISPONIBLE\",\"ubicacion\":\"Estante B-04\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Ejemplar> crearEjemplar(@Valid @RequestBody EjemplarDTO dto) {
        logger.info("POST /api/ejemplares");
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.crearEjemplar(dto));
    }

    @Operation(
            summary = "Actualizar un ejemplar existente",
            description = "Modifica los datos de un ejemplar según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejemplar actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"codigo\":\"EJ-0001\",\"libroId\":3,\"estado\":\"PRESTADO\",\"ubicacion\":\"Estante A-12\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> actualizarEjemplar(
            @Parameter(description = "ID del ejemplar a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EjemplarDTO dto) {
        logger.info("PUT /api/ejemplares/{}", id);
        return ResponseEntity.ok(ejemplarService.actualizarEjemplar(id, dto));
    }

    @Operation(
            summary = "Eliminar un ejemplar",
            description = "Elimina un ejemplar del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ejemplar eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(
            @Parameter(description = "ID del ejemplar a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /api/ejemplares/{}", id);
        ejemplarService.eliminarEjemplar(id);
        return ResponseEntity.noContent().build();
    }
}