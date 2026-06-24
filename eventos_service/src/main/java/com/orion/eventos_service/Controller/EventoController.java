package com.orion.eventos_service.Controller;

import com.orion.eventos_service.Assembler.EventoModelAssembler;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/evento")
public class EventoController {

    @Autowired
    private EventoService service;
    @Autowired
    private EventoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoResponse>> crear(
            @Valid @RequestBody EventoRequest dto) {
        Long userId = extractUserId();
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        EventoResponse nuevo = service.guardar(dto, userId);
        return ResponseEntity
                .created(linkTo(methodOn(EventoController.class).verEvento(nuevo.getIdEvento())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PostMapping("/{id}/unirse")
    public ResponseEntity<EntityModel<EventoResponse>> unirse(@PathVariable Long id) {
        Long userId = extractUserId();
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        EventoResponse actualizado = service.unirseAEvento(id, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @GetMapping(value = "/ver/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EventoResponse> verEvento(@PathVariable Long id) {
        EventoResponse evento = service.obtenerPorId(id);
        return assembler.toModel(evento);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EventoResponse>> verEventosGlobales() {
        List<EntityModel<EventoResponse>> lista = service.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(EventoController.class).verEventosGlobales()).withSelfRel());
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        Long userId = extractUserId();
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        service.eliminar(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizarevento/{id}")
    public ResponseEntity<EntityModel<EventoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest dto) {
        Long userId = extractUserId();
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        EventoResponse actualizado = service.actualizar(id, dto, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    private Long extractUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        try {
            return Long.parseLong(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
