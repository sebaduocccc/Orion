package com.Comentarios.Controller;

import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Service.ServiceComentario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comentarios")
public class ControllerComentario {

    private static final Logger log = LoggerFactory.getLogger(ControllerComentario.class);
    private final ServiceComentario service;

    @GetMapping("/{postId}/comentarios")
    public ResponseEntity<List<ResponseComentario>> comentariosPost(@PathVariable Long postId) {
        log.info("GET /api/comentarios/{}/comentarios - Viendo comentarios del post", postId);
        return ResponseEntity.ok(service.buscarPorPost(postId));
    }

    @PostMapping("/{postId}/comentar")
    public ResponseEntity<ResponseComentario> comentar(
            @PathVariable Long postId,
            @Valid @RequestBody RequestComentario r,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            log.warn("POST /api/comentarios/{}/comentar - Acceso denegado: usuario no autenticado", postId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("POST /api/comentarios/{}/comentar - userId={}", postId, userId);
        ResponseComentario creado = service.guardar(postId, r, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseComentario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RequestComentario r,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            log.warn("PUT /api/comentarios/actualizar/{} - Acceso denegado: usuario no autenticado", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("PUT /api/comentarios/actualizar/{} - userId={}", id, userId);
        ResponseComentario actualizado = service.actualizar(id, r, userId);
        return ResponseEntity.ok(actualizado);
    }
}
