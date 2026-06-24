package com.biblioteca.multas_service.controller;

import com.biblioteca.multas_service.model.Multa;
import com.biblioteca.multas_service.service.MultaService;
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
@RequestMapping("/api/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Multa>>> obtenerTodas() {
        List<EntityModel<Multa>> multas = multaService.obtenerTodas().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(MultaController.class).obtenerTodas()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(multas, selfLink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Multa>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(multaService.obtenerPorId(id)));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<CollectionModel<EntityModel<Multa>>> obtenerPorSocio(@PathVariable Long socioId) {
        List<EntityModel<Multa>> multas = multaService.obtenerPorSocio(socioId).stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(MultaController.class).obtenerPorSocio(socioId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(multas, selfLink));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<CollectionModel<EntityModel<Multa>>> obtenerPendientes() {
        List<EntityModel<Multa>> multas = multaService.obtenerPendientes().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(MultaController.class).obtenerPendientes()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(multas, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Multa>> crearMulta(@Valid @RequestBody Multa multa) {
        return ResponseEntity.ok(agregarLinks(multaService.crearMulta(multa)));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<EntityModel<Multa>> pagarMulta(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(multaService.pagarMulta(id)));
    }

    private EntityModel<Multa> agregarLinks(Multa multa) {
        Link selfLink = linkTo(methodOn(MultaController.class).obtenerPorId(multa.getId())).withSelfRel();
        Link todasLink = linkTo(methodOn(MultaController.class).obtenerTodas()).withRel("multas");
        return EntityModel.of(multa, selfLink, todasLink);
    }
}
