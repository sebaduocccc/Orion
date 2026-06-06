package com.orion.eventos_service.Controller;

import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evento")
public class EventoController {
    @Autowired
    private EventoService service;
    @PostMapping
    public ResponseEntity<EventoResponse> crear(@RequestBody EventoRequest dto) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long idCreador = (Long) auth.getCredentials();
        return ResponseEntity.ok(service.guardar(dto, idCreador));
    }
    @GetMapping
    public ResponseEntity<List<EventoResponse>> verEventosGlobales (){
        List<EventoResponse> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        service.eliminar(id,(Long) auth.getCredentials());

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/actualizarevento/{id}")
    public ResponseEntity<EventoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest dto) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        EventoResponse actualizado = service.actualizar(id, dto,(Long) auth.getCredentials());
        return ResponseEntity.ok(actualizado);
    }

}
