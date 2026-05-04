package com.Interacciones.Interacciones.Controller;

import com.Interacciones.Interacciones.Dto.InteraccionRequestDTO;
import com.Interacciones.Interacciones.Dto.InteraccionResponseDTO;
import com.Interacciones.Interacciones.Service.InteraccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interacciones")
//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ControllerInteraccion {

    private static final Logger log = LoggerFactory.getLogger(ControllerInteraccion.class);

    private final InteraccionService service;

    @PostMapping
    public ResponseEntity<InteraccionResponseDTO> guardar(@Valid @RequestBody InteraccionRequestDTO dto) {
        log.info("POST /api/interacciones - Creando interaccion");
        InteraccionResponseDTO response = service.guardar(dto);
        log.info("Interaccion creada con id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        log.info("GET /api/interacciones - Obteniendo todas");
        List<InteraccionResponseDTO> lista = service.obtenerTodos();
        if (lista.isEmpty()) {
            log.warn("No hay interacciones registradas");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay interacciones");
        }
        log.info("Se retornaron {} interacciones", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteraccionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/interacciones/{} - Buscando por id", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteraccionResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InteraccionRequestDTO dto) {
        log.info("PUT /api/interacciones/{} - Actualizando", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/interacciones/{} - Eliminando", id);
        service.eliminar(id);
        return ResponseEntity.ok("Interaccion eliminada correctamente");
    }

    // ── Endpoints especiales ──

    @GetMapping("/usuario/{userid}")
    public ResponseEntity<?> porUsuario(@PathVariable Long userid) {
        log.info("GET /api/interacciones/usuario/{}", userid);
        List<InteraccionResponseDTO> lista = service.buscarPorUsuario(userid);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sin interacciones para ese usuario");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/post/{postid}")
    public ResponseEntity<?> porPost(@PathVariable Long postid) {
        log.info("GET /api/interacciones/post/{}", postid);
        List<InteraccionResponseDTO> lista = service.buscarPorPost(postid);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sin interacciones para ese post");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> porTipo(@PathVariable String tipo) {
        log.info("GET /api/interacciones/tipo/{}", tipo);
        List<InteraccionResponseDTO> lista = service.buscarPorTipo(tipo);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sin interacciones de ese tipo");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/total/post/{postid}")
    public ResponseEntity<Map<String, Object>> totalPorPostYTipo(
            @PathVariable Long postid,
            @RequestParam String tipo) {
        log.info("GET /api/interacciones/total/post/{} tipo={}", postid, tipo);
        long total = service.contarPorPostYTipo(postid, tipo);
        return ResponseEntity.ok(Map.of("postid", postid, "tipo", tipo, "total", total));
    }
}