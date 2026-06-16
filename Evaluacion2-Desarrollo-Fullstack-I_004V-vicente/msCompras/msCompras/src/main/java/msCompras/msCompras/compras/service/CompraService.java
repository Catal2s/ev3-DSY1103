package msCompras.msCompras.compras.service;

import msCompras.msCompras.compras.dto.CompraDTO;
import msCompras.msCompras.compras.model.Compra;
import msCompras.msCompras.compras.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);
    private final CompraRepository compraRepository;

    public List<Compra> listarCompras() {
        logger.info("Listando todas las compras");
        return compraRepository.findAll();
    }

    public List<Compra> listarPorProveedor(Long proveedorId) {
        logger.info("Listando compras del proveedor id: {}", proveedorId);
        return compraRepository.findByProveedorId(proveedorId);
    }

    public List<Compra> listarPorEstado(String estado) {
        logger.info("Listando compras con estado: {}", estado);
        return compraRepository.findByEstado(estado);
    }

    public Compra obtenerCompraPorId(Long id) {
        logger.info("Buscando compra con id: {}", id);
        return compraRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Compra con id {} no encontrada", id);
                    return new RuntimeException("Compra no encontrada con id: " + id);
                });
    }

    public Compra crearCompra(CompraDTO dto) {
        logger.info("Creando nueva compra para proveedor id: {}", dto.getProveedorId());
        Compra compra = new Compra();
        compra.setProveedorId(dto.getProveedorId());
        compra.setEstado(dto.getEstado());
        compra.setTotal(dto.getTotal());
        compra.setFechaCompra(dto.getFechaCompra());
        compra.setDescripcion(dto.getDescripcion());
        return compraRepository.save(compra);
    }

    public Compra actualizarCompra(Long id, CompraDTO dto) {
        logger.info("Actualizando compra con id: {}", id);
        Compra compra = obtenerCompraPorId(id);
        compra.setProveedorId(dto.getProveedorId());
        compra.setEstado(dto.getEstado());
        compra.setTotal(dto.getTotal());
        compra.setFechaCompra(dto.getFechaCompra());
        compra.setDescripcion(dto.getDescripcion());
        return compraRepository.save(compra);
    }

    public void eliminarCompra(Long id) {
        logger.info("Eliminando compra con id: {}", id);
        compraRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Compra con id {} no encontrada para eliminar", id);
                    return new RuntimeException("Compra no encontrada con id: " + id);
                });
        compraRepository.deleteById(id);
    }
}