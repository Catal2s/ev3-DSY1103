package com.biblioteca.prestamos_service.controller;

import com.biblioteca.prestamos_service.dto.PrestamoRequestDTO;
import com.biblioteca.prestamos_service.dto.PrestamoResponseDTO;
import com.biblioteca.prestamos_service.service.PrestamoService;
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
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PrestamoResponseDTO>>> obtenerTodos() {
        List<EntityModel<PrestamoResponseDTO>> prestamos = prestamoService.obtenerTodos().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(PrestamoController.class).obtenerTodos()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(prestamos, selfLink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PrestamoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(prestamoService.obtenerPorId(id)));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<CollectionModel<EntityModel<PrestamoResponseDTO>>> obtenerPorSocio(@PathVariable Long socioId) {
        List<EntityModel<PrestamoResponseDTO>> prestamos = prestamoService.obtenerPorSocio(socioId).stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(PrestamoController.class).obtenerPorSocio(socioId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(prestamos, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PrestamoResponseDTO>> crearPrestamo(@Valid @RequestBody PrestamoRequestDTO request) {
        return ResponseEntity.ok(agregarLinks(prestamoService.crearPrestamo(request)));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<EntityModel<PrestamoResponseDTO>> devolverPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(prestamoService.devolverPrestamo(id)));
    }

    private EntityModel<PrestamoResponseDTO> agregarLinks(PrestamoResponseDTO dto) {
        Link selfLink = linkTo(methodOn(PrestamoController.class).obtenerPorId(dto.getId())).withSelfRel();
        Link todosLink = linkTo(methodOn(PrestamoController.class).obtenerTodos()).withRel("prestamos");
        return EntityModel.of(dto, selfLink, todosLink);
    }
}
