package com.biblioteca.pagos_service.controller;

import com.biblioteca.pagos_service.dto.PagoRequestDTO;
import com.biblioteca.pagos_service.dto.PagoResponseDTO;
import com.biblioteca.pagos_service.service.PagoService;
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
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> obtenerTodos() {
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.obtenerTodos().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(PagoController.class).obtenerTodos()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(pagos, selfLink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(pagoService.obtenerPorId(id)));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> obtenerPorSocio(@PathVariable Long socioId) {
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.obtenerPorSocio(socioId).stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(PagoController.class).obtenerPorSocio(socioId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(pagos, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PagoResponseDTO>> registrarPago(@Valid @RequestBody PagoRequestDTO request) {
        return ResponseEntity.ok(agregarLinks(pagoService.registrarPago(request)));
    }

    private EntityModel<PagoResponseDTO> agregarLinks(PagoResponseDTO dto) {
        Link selfLink = linkTo(methodOn(PagoController.class).obtenerPorId(dto.getId())).withSelfRel();
        Link todosLink = linkTo(methodOn(PagoController.class).obtenerTodos()).withRel("pagos");
        return EntityModel.of(dto, selfLink, todosLink);
    }
}
