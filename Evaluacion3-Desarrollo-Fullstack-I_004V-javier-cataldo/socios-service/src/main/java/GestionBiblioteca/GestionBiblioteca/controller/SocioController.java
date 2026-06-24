package GestionBiblioteca.GestionBiblioteca.controller;

import GestionBiblioteca.GestionBiblioteca.dto.SocioRequestDTO;
import GestionBiblioteca.GestionBiblioteca.dto.SocioResponseDTO;
import GestionBiblioteca.GestionBiblioteca.service.SocioService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SocioResponseDTO>>> obtenerTodos() {
        List<SocioResponseDTO> socios = socioService.obtenerTodos();

        List<EntityModel<SocioResponseDTO>> sociosModel = socios.stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(SocioController.class).obtenerTodos()).withSelfRel();
        CollectionModel<EntityModel<SocioResponseDTO>> model = CollectionModel.of(sociosModel, selfLink);

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SocioResponseDTO>> obtenerPorId(@PathVariable Long id) {
        SocioResponseDTO socio = socioService.obtenerPorId(id);
        return ResponseEntity.ok(agregarLinks(socio));
    }

    @PostMapping
    public ResponseEntity<EntityModel<SocioResponseDTO>> crearSocio(@Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socio = socioService.crearSocio(request);
        return ResponseEntity.ok(agregarLinks(socio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SocioResponseDTO>> actualizarSocio(@PathVariable Long id, @Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socio = socioService.actualizarSocio(id, request);
        return ResponseEntity.ok(agregarLinks(socio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<SocioResponseDTO> agregarLinks(SocioResponseDTO socio) {
        Link selfLink = linkTo(methodOn(SocioController.class).obtenerPorId(socio.getId())).withSelfRel();
        Link todosLink = linkTo(methodOn(SocioController.class).obtenerTodos()).withRel("socios");
        return EntityModel.of(socio, selfLink, todosLink);
    }
}
