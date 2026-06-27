package msLibros.msLibros.libros.controller;

import msLibros.msLibros.libros.dto.LibroDTO;
import msLibros.msLibros.libros.dto.LibroResponseDTO;
import msLibros.msLibros.libros.model.Libro;
import msLibros.msLibros.libros.service.LibroService;
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
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Gestión de libros del sistema de bibliotecas digitales. Consume datos de Autores, Categorías y Editoriales vía WebClient para enriquecer las respuestas.")
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);
    private final LibroService libroService;

    @Operation(
            summary = "Listar todos los libros",
            description = "Retorna la lista completa de libros, enriquecida con el nombre del autor, categoría y editorial obtenidos mediante comunicación REST con los otros microservicios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"titulo\":\"Cien años de soledad\",\"isbn\":\"978-0307474728\",\"autorId\":1,\"nombreAutor\":\"Gabriel García Márquez\",\"categoriaId\":1,\"nombreCategoria\":\"Novela\",\"editorialId\":1,\"nombreEditorial\":\"Penguin Random House\",\"anioPublicacion\":1967,\"descripcion\":\"Una de las obras más importantes del realismo mágico\"}]")))
    })
    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> listarLibros() {
        logger.info("GET /api/libros");
        return ResponseEntity.ok(libroService.listarLibros());
    }

    @Operation(
            summary = "Obtener un libro por ID",
            description = "Retorna los datos de un libro específico enriquecido con el nombre del autor, categoría y editorial."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"titulo\":\"Cien años de soledad\",\"isbn\":\"978-0307474728\",\"autorId\":1,\"nombreAutor\":\"Gabriel García Márquez\",\"categoriaId\":1,\"nombreCategoria\":\"Novela\",\"editorialId\":1,\"nombreEditorial\":\"Penguin Random House\",\"anioPublicacion\":1967,\"descripcion\":\"Una de las obras más importantes del realismo mágico\"}"))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibro(
            @Parameter(description = "ID del libro a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /api/libros/{}", id);
        return ResponseEntity.ok(libroService.obtenerLibroPorId(id));
    }

    @Operation(
            summary = "Crear un nuevo libro",
            description = "Registra un nuevo libro en el sistema, validando los datos obligatorios y las referencias a autor, categoría y editorial."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":10,\"titulo\":\"La sombra del viento\",\"isbn\":\"978-8408172177\",\"autorId\":2,\"categoriaId\":1,\"editorialId\":2,\"anioPublicacion\":2001,\"descripcion\":\"Novela ambientada en la Barcelona de posguerra\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO dto) {
        logger.info("POST /api/libros");
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.crearLibro(dto));
    }

    @Operation(
            summary = "Actualizar un libro existente",
            description = "Modifica los datos de un libro según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"id\":1,\"titulo\":\"Cien años de soledad\",\"isbn\":\"978-0307474728\",\"autorId\":1,\"categoriaId\":1,\"editorialId\":1,\"anioPublicacion\":1967,\"descripcion\":\"Una de las obras más importantes del realismo mágico\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(
            @Parameter(description = "ID del libro a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody LibroDTO dto) {
        logger.info("PUT /api/libros/{}", id);
        return ResponseEntity.ok(libroService.actualizarLibro(id, dto));
    }

    @Operation(
            summary = "Eliminar un libro",
            description = "Elimina un libro del sistema según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(
            @Parameter(description = "ID del libro a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /api/libros/{}", id);
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}