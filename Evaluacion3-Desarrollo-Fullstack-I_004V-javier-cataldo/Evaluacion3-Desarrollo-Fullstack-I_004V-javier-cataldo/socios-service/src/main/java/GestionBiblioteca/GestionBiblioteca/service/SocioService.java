package GestionBiblioteca.GestionBiblioteca.service;

import GestionBiblioteca.GestionBiblioteca.dto.SocioRequestDTO;
import GestionBiblioteca.GestionBiblioteca.dto.SocioResponseDTO;
import GestionBiblioteca.GestionBiblioteca.model.Socio;
import GestionBiblioteca.GestionBiblioteca.repository.SocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocioService {

    private final Logger log = LoggerFactory.getLogger(SocioService.class);
    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    //Convierte de entidad a DTO pa devolver al front
    private SocioResponseDTO convertirAResponse(Socio socio) {
        SocioResponseDTO dto = new SocioResponseDTO();
        dto.setId(socio.getId());
        dto.setNombre(socio.getNombre());
        dto.setRut(socio.getRut());
        dto.setEmail(socio.getEmail());
        dto.setTelefono(socio.getTelefono());
        dto.setActivo(socio.isActivo());
        return dto;
    }

    public List<SocioResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los socios");
        return socioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public SocioResponseDTO obtenerPorId(Long id) {
        log.info("Buscando socio por id: {}", id);
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + id));
        return convertirAResponse(socio);
    }

    public SocioResponseDTO crearSocio(SocioRequestDTO request) {
        //Valida que el RUT no este repetido
        if (socioRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("El RUT " + request.getRut() + " ya esta registrado.");
        }
        //Valida que el email no este repetido tmb
        if (socioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email " + request.getEmail() + " ya esta registrado.");
        }

        Socio socio = new Socio();
        socio.setNombre(request.getNombre());
        socio.setRut(request.getRut());
        socio.setEmail(request.getEmail());
        socio.setTelefono(request.getTelefono());
        socio.setActivo(true);

        log.info("Creando un nuevo socio con RUT: {}", request.getRut());
        return convertirAResponse(socioRepository.save(socio));
    }

    //PUT - Actualizar socio existente
    public SocioResponseDTO actualizarSocio(Long id, SocioRequestDTO request) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + id));

        socio.setNombre(request.getNombre());
        socio.setRut(request.getRut());
        socio.setEmail(request.getEmail());
        socio.setTelefono(request.getTelefono());

        log.info("Actualizando socio con id: {}", id);
        return convertirAResponse(socioRepository.save(socio));
    }

    public void eliminarSocio(Long id) {
        log.info("Eliminando socio con id: {}", id);
        socioRepository.deleteById(id);
    }
}
