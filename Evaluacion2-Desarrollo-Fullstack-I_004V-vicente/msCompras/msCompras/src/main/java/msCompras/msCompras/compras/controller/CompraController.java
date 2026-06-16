package msCompras.msCompras.compras.controller;

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
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);
    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<List<Compra>> listarCompras() {
        logger.info("GET /api/compras");
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerCompra(@PathVariable Long id) {
        logger.info("GET /api/compras/{}", id);
        return ResponseEntity.ok(compraService.obtenerCompraPorId(id));
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Compra>> listarPorProveedor(@PathVariable Long proveedorId) {
        logger.info("GET /api/compras/proveedor/{}", proveedorId);
        return ResponseEntity.ok(compraService.listarPorProveedor(proveedorId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Compra>> listarPorEstado(@PathVariable String estado) {
        logger.info("GET /api/compras/estado/{}", estado);
        return ResponseEntity.ok(compraService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@Valid @RequestBody CompraDTO dto) {
        logger.info("POST /api/compras");
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.crearCompra(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @Valid @RequestBody CompraDTO dto) {
        logger.info("PUT /api/compras/{}", id);
        return ResponseEntity.ok(compraService.actualizarCompra(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        logger.info("DELETE /api/compras/{}", id);
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}