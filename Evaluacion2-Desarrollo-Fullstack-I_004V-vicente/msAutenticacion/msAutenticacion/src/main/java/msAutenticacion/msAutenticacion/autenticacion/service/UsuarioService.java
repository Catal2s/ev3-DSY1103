package msAutenticacion.msAutenticacion.autenticacion.service;

import msAutenticacion.msAutenticacion.autenticacion.dto.UsuarioDTO;
import msAutenticacion.msAutenticacion.autenticacion.model.Usuario;
import msAutenticacion.msAutenticacion.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        logger.info("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarUsuariosActivos() {
        logger.info("Listando usuarios activos");
        return usuarioRepository.findByActivoTrue();
    }

    public List<Usuario> listarPorRol(String rol) {
        logger.info("Listando usuarios con rol: {}", rol);
        return usuarioRepository.findByRol(rol);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        logger.info("Buscando usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Usuario con id {} no encontrado", id);
                    return new RuntimeException("Usuario no encontrado con id: " + id);
                });
    }

    public Usuario obtenerPorEmail(String email) {
        logger.info("Buscando usuario con email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario con email {} no encontrado", email);
                    return new RuntimeException("Usuario no encontrado con email: " + email);
                });
    }

    public Usuario crearUsuario(UsuarioDTO dto) {
        logger.info("Creando nuevo usuario: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.isActivo());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDTO dto) {
        logger.info("Actualizando usuario con id: {}", id);
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.isActivo());
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        logger.info("Eliminando usuario con id: {}", id);
        usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Usuario con id {} no encontrado para eliminar", id);
                    return new RuntimeException("Usuario no encontrado con id: " + id);
                });
        usuarioRepository.deleteById(id);
    }
}