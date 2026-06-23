package msProveedores.msProveedores.proveedores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msProveedores.msProveedores.proveedores.dto.ProveedorDTO;
import msProveedores.msProveedores.proveedores.model.Proveedor;
import msProveedores.msProveedores.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Gestión de proveedores de la biblioteca")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);
    private final ProveedorService proveedorService;

    @Operation(summary = "Listar todos los proveedores")
    @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        logger.info("GET /api/proveedores");
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @Operation(summary = "Listar proveedores activos")
    @ApiResponse(responseCode = "200", description = "Lista de proveedores activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> listarActivos() {
        logger.info("GET /api/proveedores/activos");
        return ResponseEntity.ok(proveedorService.listarProveedoresActivos());
    }

    @Operation(summary = "Obtener proveedor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedor(@PathVariable Long id) {
        logger.info("GET /api/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.obtenerProveedorPorId(id));
    }

    @Operation(summary = "Crear nuevo proveedor")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o RUT duplicado")
    })
    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@Valid @RequestBody ProveedorDTO dto) {
        logger.info("POST /api/proveedores");
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crearProveedor(dto));
    }

    @Operation(summary = "Actualizar proveedor existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        logger.info("PUT /api/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id, dto));
    }

    @Operation(summary = "Eliminar proveedor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Proveedor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        logger.info("DELETE /api/proveedores/{}", id);
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
