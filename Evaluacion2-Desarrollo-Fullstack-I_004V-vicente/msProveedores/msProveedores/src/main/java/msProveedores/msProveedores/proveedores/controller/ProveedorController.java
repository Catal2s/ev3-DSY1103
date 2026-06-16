package msProveedores.msProveedores.proveedores.controller;

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
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);
    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        logger.info("GET /api/proveedores");
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> listarActivos() {
        logger.info("GET /api/proveedores/activos");
        return ResponseEntity.ok(proveedorService.listarProveedoresActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedor(@PathVariable Long id) {
        logger.info("GET /api/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.obtenerProveedorPorId(id));
    }

    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@Valid @RequestBody ProveedorDTO dto) {
        logger.info("POST /api/proveedores");
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crearProveedor(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        logger.info("PUT /api/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        logger.info("DELETE /api/proveedores/{}", id);
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}