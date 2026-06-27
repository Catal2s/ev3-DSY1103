package msCategoria.msCategoria.categorias.controller;

import msCategoria.msCategoria.categorias.dto.CategoriaDTO;
import msCategoria.msCategoria.categorias.model.Categoria;
import msCategoria.msCategoria.categorias.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías del sistema de bibliotecas digitales")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    private final CategoriaService categoriaService;

    @Operation(
            summary = "Listar todas las categorías",
            description = "Retorna la lista completa de categorías registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"nombre\":\"Novela\",\"descripcion\":\"Obras de ficción narrativa\"}]")))
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        logger.info("GET /api/categorias");
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @Operation(
            summary = "Obtener una categoría por ID",
            description = "Retorna los datos de una categoría específica según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Novela\",\"descripcion\":\"Obras de ficción narrativa\"}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(
            @Parameter(description = "ID de la categoría a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /api/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    @Operation(
            summary = "Crear una nueva categoría",
            description = "Registra una nueva categoría en el sistema, validando los datos obligatorios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":5,\"nombre\":\"Ciencia Ficción\",\"descripcion\":\"Obras especulativas y futuristas\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody CategoriaDTO dto) {
        logger.info("POST /api/categorias");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(dto));
    }

    @Operation(
            summary = "Actualizar una categoría existente",
            description = "Modifica los datos de una categoría según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"nombre\":\"Novela\",\"descripcion\":\"Obras de ficción narrativa\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO dto) {
        logger.info("PUT /api/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, dto));
    }

    @Operation(
            summary = "Eliminar una categoría",
            description = "Elimina una categoría del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /api/categorias/{}", id);
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}