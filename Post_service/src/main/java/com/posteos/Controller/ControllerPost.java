package com.posteos.Controller;

import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Repository.Repository_Post;
import com.posteos.Service.ServicePost;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin(origins = "http://localhost:5173")
public class ControllerPost {

    @Autowired
    private ServicePost service;
    @Autowired
    private Repository_Post repository_Post;


    // CRUD


    //CREATE
    @PostMapping
    public ResponseEntity<?> guardar(
            @Valid @RequestBody PostRequestDTO dto,
            @AuthenticationPrincipal Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        log.info("POST /api/posts - Creando post para usuario id={}", userId);
        dto.setUserId(userId);
        PostResponseDTO response = service.guardar(dto);
        log.info("Post creado con id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // READ
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        log.info("GET /api/posts - Obteniendo todos los posts");
        List<PostResponseDTO> lista = service.obtenerTodos();
        if (lista.isEmpty()) {
            log.warn("No hay posts registrados");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO HAY POSTS");
        }
        log.info("Se retornaron {} posts", lista.size());
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> obtenerTodosPorUsuario(@PathVariable Long userId) {
        log.info("GET /api/posts/user/{} - Posts del usuario", userId);
        List<PostResponseDTO> posts = service.buscarPostDeUsuario(userId);
        if (posts.isEmpty()) {
            log.warn("No se encontraron posts para usuario id={}", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> obtenerCountLikesUsuario(@PathVariable Long userId) {
        log.info("GET /api/posts/user/{}/count - Contando posts del usuario", userId);
        return ResponseEntity.ok(service.totalDePostDeUsuario(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/posts/{} - Buscando post por id", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> verPostsUser(@PathVariable Long id) {
        log.info("GET /api/posts/usuario/{} - Posts del usuario", id);
        List<PostResponseDTO> lista = service.buscarUser(id);
        if (lista.isEmpty()) {
            log.warn("No se encontraron posts para usuario id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sin posts para ese usuario");
        }
        return ResponseEntity.ok(lista);
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO dto) {
        log.info("PUT /api/posts/{} - Actualizando post", id);
        PostResponseDTO actualizado = service.actualizar(id, dto);
        log.info("Post id={} actualizado correctamente", id);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrar(@PathVariable Long id) {
        log.info("DELETE /api/posts/{} - Eliminando post", id);
        service.eliminar(id);
        log.info("Post id={} eliminado correctamente", id);
        return ResponseEntity.ok("Post eliminado correctamente");
    }




    //FRONTEND


    @GetMapping("/feed")
    public Page<Post> obtenerTodosFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/posts/feed - página={} tamaño={}", page, size);
        return service.cargarFeedPrincipal(page, size);
    }


    @GetMapping("/feed/{id}")
    public Page<Post> obtenerFeedUsuario(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long id) {
        log.info("GET /api/posts/feed/{} - página={} tamaño={}", id, page, size);
        return service.cargarFeedUsuario(page, size, id);
    }

}