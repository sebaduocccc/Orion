package com.orion.Grupos_service.Controller;

import com.orion.Grupos_service.Assembler.GrupoModelAssembler;
import com.orion.Grupos_service.Dto.RequestGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import com.orion.Grupos_service.Service.ServiceGrupo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/grupo")
public class ControllerGrupo {
    @Autowired
    private ServiceGrupo service;
    @Autowired
    private GrupoModelAssembler assembler;


    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ResponseGrupo>> crearGrupo(@Valid @RequestBody RequestGrupo dto) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        ResponseGrupo nuevo = service.guardar(dto, userId);

        return ResponseEntity
                .created(linkTo(methodOn(ControllerGrupo.class).verGrupos(nuevo.getIdGrupo())).toUri())
                .body(assembler.toModel(nuevo));
    }
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ResponseGrupo> verGrupos(@PathVariable Long id) {
        ResponseGrupo grupo = service.obtenerPorId(id);
        return assembler.toModel(grupo);
    }
    @GetMapping( produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ResponseGrupo>> verTodosLosGrupos() {
        List<ResponseGrupo> grupos = service.obtenerTodos();
        return assembler.toCollectionModel(grupos);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        service.eliminar(id, userId);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ResponseGrupo>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RequestGrupo dto) {

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        ResponseGrupo actualizado = service.actualizar(id, dto, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }
    @PostMapping("/{id}/unirse")
    public ResponseEntity<EntityModel<ResponseGrupo>> unirse(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        ResponseGrupo actualizado = service.unirseAGrupo(id, userId);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

}
