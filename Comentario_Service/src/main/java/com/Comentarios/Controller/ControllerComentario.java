package com.Comentarios.Controller;

import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Service.ServiceComentario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ControllerComentario {

    private static final Logger log = LoggerFactory.getLogger(ControllerComentario.class);
    private final ServiceComentario service;

    @PostMapping
    public ResponseEntity<ResponseComentario> crear(@RequestBody RequestComentario dto) {
        log.info("POST /api/comentarios - Creando comentario para post id={}", dto.getPostId());
        ResponseComentario creado = service.guardar(dto);
        log.info("POST /api/comentarios - Comentario creado con id={}", creado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<ResponseComentario>> obtenerTodos() {
        log.info("GET /api/comentarios - Obteniendo todos los comentarios");
        List<ResponseComentario> lista = service.obtenerTodos();
        log.info("GET /api/comentarios - Total encontrados: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseComentario> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/comentarios/{} - Buscando comentario", id);
        ResponseComentario comentario = service.buscarPorId(id);
        log.info("GET /api/comentarios/{} - Encontrado", id);
        return ResponseEntity.ok(comentario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseComentario> actualizar(@PathVariable Long id, @RequestBody RequestComentario dto) {
        log.info("PUT /api/comentarios/{} - Actualizando comentario", id);
        ResponseComentario actualizado = service.actualizar(id, dto);
        log.info("PUT /api/comentarios/{} - Actualizado correctamente", id);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/comentarios/{} - Eliminando comentario", id);
        service.eliminar(id);
        log.info("DELETE /api/comentarios/{} - Eliminado correctamente", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ResponseComentario>> obtenerPorPost(@PathVariable Long postId) {
        log.info("GET /api/comentarios/post/{} - Buscando comentarios", postId);
        List<ResponseComentario> lista = service.buscarPorPost(postId);
        log.info("GET /api/comentarios/post/{} - Total encontrados: {}", postId, lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<ResponseComentario>> obtenerPorUsuario(@PathVariable Long userId) {
        log.info("GET /api/comentarios/usuario/{} - Buscando comentarios", userId);
        List<ResponseComentario> lista = service.buscarPorUsuario(userId);
        log.info("GET /api/comentarios/usuario/{} - Total encontrados: {}", userId, lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/post/{postId}/total")
    public ResponseEntity<Long> contarPorPost(@PathVariable Long postId) {
        log.info("GET /api/comentarios/post/{}/total - Contando comentarios", postId);
        long total = service.contarcomentarios(postId);
        log.info("GET /api/comentarios/post/{}/total - Total: {}", postId, total);
        return ResponseEntity.ok(total);
    }
}