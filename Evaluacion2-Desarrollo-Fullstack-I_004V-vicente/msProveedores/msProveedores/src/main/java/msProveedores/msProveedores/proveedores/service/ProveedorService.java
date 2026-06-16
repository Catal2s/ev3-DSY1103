package msProveedores.msProveedores.proveedores.service;

import msProveedores.msProveedores.proveedores.dto.ProveedorDTO;
import msProveedores.msProveedores.proveedores.model.Proveedor;
import msProveedores.msProveedores.proveedores.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);
    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> listarProveedores() {
        logger.info("Listando todos los proveedores");
        return proveedorRepository.findAll();
    }

    public List<Proveedor> listarProveedoresActivos() {
        logger.info("Listando proveedores activos");
        return proveedorRepository.findByActivoTrue();
    }

    public Proveedor obtenerProveedorPorId(Long id) {
        logger.info("Buscando proveedor con id: {}", id);
        return proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Proveedor con id {} no encontrado", id);
                    return new RuntimeException("Proveedor no encontrado con id: " + id);
                });
    }

    public Proveedor crearProveedor(ProveedorDTO dto) {
        logger.info("Creando nuevo proveedor: {}", dto.getNombre());
        if (proveedorRepository.existsByRut(dto.getRut())) {
            throw new RuntimeException("Ya existe un proveedor con el RUT: " + dto.getRut());
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setRut(dto.getRut());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setActivo(dto.isActivo());
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, ProveedorDTO dto) {
        logger.info("Actualizando proveedor con id: {}", id);
        Proveedor proveedor = obtenerProveedorPorId(id);
        proveedor.setNombre(dto.getNombre());
        proveedor.setRut(dto.getRut());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setActivo(dto.isActivo());
        return proveedorRepository.save(proveedor);
    }

    public void eliminarProveedor(Long id) {
        logger.info("Eliminando proveedor con id: {}", id);
        proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Proveedor con id {} no encontrado para eliminar", id);
                    return new RuntimeException("Proveedor no encontrado con id: " + id);
                });
        proveedorRepository.deleteById(id);
    }
}