package com.orion.interaccion.Controller;


import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Service.FollowService;
import com.orion.interaccion.Service.InteraccionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/interacciones")
@SecurityRequirement(name = "bearerAuth")
public class InteraccionController {

    @Autowired
    private InteraccionService interaccionService;

    @Autowired
    private FollowService followService;


    @PostMapping("/post/{postId}/like")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long postId,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        log.info("POST /interacciones/post/{}/like - Usuario id={}", postId, userId);
        boolean isLiked = interaccionService.toggleLike(userId, postId);
        log.info("Toggle like completado: usuario={} post={} estado={}", userId, postId, isLiked ? "liked" : "unliked");
        return ResponseEntity.ok(isLiked);
    }


    @GetMapping("/post/{postId}/like/count")
    public ResponseEntity<Long> contarLikes(@PathVariable Long postId) {
        log.info("GET /interacciones/post/{}/like/count", postId);
        return ResponseEntity.ok(interaccionService.obtenerTotalLikes(postId));
    }


    @GetMapping("/post/{postId}/like/status")
    public ResponseEntity<?> checkIsLikedByMe(
            @PathVariable Long postId,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        log.info("GET /interacciones/post/{}/like/status - Usuario id={}", postId, userId);
        return ResponseEntity.ok(interaccionService.verificarSiLikeo(userId, postId));
    }


    @PostMapping("/usuarios/{seguidoId}/follow")
    public ResponseEntity<?> toggleFollow(
            @PathVariable Long seguidoId,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        log.info("POST /interacciones/usuarios/{}/follow - Usuario id={}", seguidoId, userId);
        boolean isFollowing = followService.toggleFollow(userId, seguidoId);
        log.info("Toggle follow completado: seguidor={} seguido={} estado={}", userId, seguidoId, isFollowing ? "siguiendo" : "unfollowed");
        return ResponseEntity.ok(isFollowing);
    }


    @GetMapping("/usuarios/{usuarioId}/seguidores/count")
    public ResponseEntity<Long> getSeguidoresCount(@PathVariable Long usuarioId) {
        log.info("GET /interacciones/usuarios/{}/seguidores/count", usuarioId);
        return ResponseEntity.ok(followService.obtenerContadorSeguidores(usuarioId));
    }

    @GetMapping("/usuarios/{usuarioId}/seguidos/count")
    public ResponseEntity<Long> getSeguidosCount(@PathVariable Long usuarioId) {
        log.info("GET /interacciones/usuarios/{}/seguidos/count", usuarioId);
        return ResponseEntity.ok(followService.obtenerContadorSeguidos(usuarioId));
    }

    @GetMapping("/usuarios/{seguidoId}/siguiendo")
    public ResponseEntity<?> checkFollowingStatus(
            @PathVariable Long seguidoId,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        log.info("GET /interacciones/usuarios/{}/siguiendo - Usuario id={}", seguidoId, userId);
        return ResponseEntity.ok(followService.verificarSiSigue(userId, seguidoId));
    }
}
