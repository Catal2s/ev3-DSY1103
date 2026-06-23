package msCompras.msCompras.compras.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msCompras.msCompras.compras.dto.CompraDTO;
import msCompras.msCompras.compras.model.Compra;
import msCompras.msCompras.compras.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Gestión de órdenes de compra a proveedores")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);
    private final CompraService compraService;

    @Operation(summary = "Listar todas las compras")
    @ApiResponse(responseCode = "200", description = "Lista de compras obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Compra>> listarCompras() {
        logger.info("GET /api/compras");
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @Operation(summary = "Obtener compra por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra encontrada"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerCompra(@PathVariable Long id) {
        logger.info("GET /api/compras/{}", id);
        return ResponseEntity.ok(compraService.obtenerCompraPorId(id));
    }

    @Operation(summary = "Listar compras por proveedor")
    @ApiResponse(responseCode = "200", description = "Lista de compras del proveedor")
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Compra>> listarPorProveedor(@PathVariable Long proveedorId) {
        logger.info("GET /api/compras/proveedor/{}", proveedorId);
        return ResponseEntity.ok(compraService.listarPorProveedor(proveedorId));
    }

    @Operation(summary = "Listar compras por estado")
    @ApiResponse(responseCode = "200", description = "Lista de compras filtrada por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Compra>> listarPorEstado(@PathVariable String estado) {
        logger.info("GET /api/compras/estado/{}", estado);
        return ResponseEntity.ok(compraService.listarPorEstado(estado));
    }

    @Operation(summary = "Crear nueva compra")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Compra creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Compra> crearCompra(@Valid @RequestBody CompraDTO dto) {
        logger.info("POST /api/compras");
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.crearCompra(dto));
    }

    @Operation(summary = "Actualizar compra existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compra actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @Valid @RequestBody CompraDTO dto) {
        logger.info("PUT /api/compras/{}", id);
        return ResponseEntity.ok(compraService.actualizarCompra(id, dto));
    }

    @Operation(summary = "Eliminar compra por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Compra eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        logger.info("DELETE /api/compras/{}", id);
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}
