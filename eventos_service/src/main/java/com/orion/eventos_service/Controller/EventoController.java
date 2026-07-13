package com.orion.eventos_service.Controller;

import com.orion.eventos_service.Assembler.EventoModelAssembler;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Service.EventoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/evento")
public class EventoController {

    @Autowired
    private EventoService service;
    @Autowired
    private EventoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoResponse>> crear(
            @Valid @RequestBody EventoRequest dto,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        log.info("POST /api/evento - Creando evento solicitado por usuario={}", userId);
        EventoResponse nuevo = service.guardar(dto, userId);
        return ResponseEntity
                .created(linkTo(methodOn(EventoController.class).verEvento(nuevo.getIdEvento())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PostMapping("/{id}/unirse")
    public ResponseEntity<EntityModel<EventoResponse>> unirse(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        log.info("POST /api/evento/{}/unirse - Usuario={}", id, userId);
        EventoResponse actualizado = service.unirseAEvento(id, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @GetMapping(value = "/ver/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EventoResponse> verEvento(@PathVariable Long id) {
        log.info("GET /api/evento/ver/{}", id);
        EventoResponse evento = service.obtenerPorId(id);
        return assembler.toModel(evento);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EventoResponse>> verEventosGlobales() {
        log.info("GET /api/evento - Listando eventos");
        List<EntityModel<EventoResponse>> lista = service.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(EventoController.class).verEventosGlobales()).withSelfRel());
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        log.info("DELETE /api/evento/borrar/{} - Solicitado por usuario={}", id, userId);
        service.eliminar(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizarevento/{id}")
    public ResponseEntity<EntityModel<EventoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest dto,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        log.info("PUT /api/evento/actualizarevento/{} - Solicitado por usuario={}", id, userId);
        EventoResponse actualizado = service.actualizar(id, dto, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }
}
