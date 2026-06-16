package msAutenticacion.msAutenticacion.autenticacion.controller;

import msAutenticacion.msAutenticacion.autenticacion.dto.UsuarioDTO;
import msAutenticacion.msAutenticacion.autenticacion.model.Usuario;
import msAutenticacion.msAutenticacion.autenticacion.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        logger.info("GET /api/usuarios");
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        logger.info("GET /api/usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        logger.info("GET /api/usuarios/email/{}", email);
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> listarPorRol(@PathVariable String rol) {
        logger.info("GET /api/usuarios/rol/{}", rol);
        return ResponseEntity.ok(usuarioService.listarPorRol(rol));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        logger.info("GET /api/usuarios/activos");
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        logger.info("POST /api/usuarios");
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        logger.info("PUT /api/usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        logger.info("DELETE /api/usuarios/{}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}