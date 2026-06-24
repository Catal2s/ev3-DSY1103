package com.biblioteca.reservas_service.controller;

import com.biblioteca.reservas_service.model.Reserva;
import com.biblioteca.reservas_service.service.ReservaService;
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
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> obtenerTodas() {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerTodas().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(ReservaController.class).obtenerTodas()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(reservas, selfLink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reserva>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(reservaService.obtenerPorId(id)));
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> obtenerPorSocio(@PathVariable Long socioId) {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerPorSocio(socioId).stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(ReservaController.class).obtenerPorSocio(socioId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(reservas, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Reserva>> crearReserva(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.ok(agregarLinks(reservaService.crearReserva(reserva)));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<Reserva>> cancelarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(agregarLinks(reservaService.cancelarReserva(id)));
    }

    private EntityModel<Reserva> agregarLinks(Reserva reserva) {
        Link selfLink = linkTo(methodOn(ReservaController.class).obtenerPorId(reserva.getId())).withSelfRel();
        Link todasLink = linkTo(methodOn(ReservaController.class).obtenerTodas()).withRel("reservas");
        return EntityModel.of(reserva, selfLink, todasLink);
    }
}
