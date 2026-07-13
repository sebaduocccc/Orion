package com.orion.chatservice.Controller;


import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Service.MensajeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/chat")
public class HistorialController {


    @Autowired
    private MensajeService mensajeService;


    @GetMapping("/historial")
    public ResponseEntity<Page<Mensaje>> obtenerHistorial(
            @RequestParam Long user1,
            @RequestParam Long user2,
            @RequestParam int page,
            @RequestParam int size
    ) {
        log.info("GET /api/chat/historial - user1={} user2={}", user1, user2);
        return ResponseEntity.ok(mensajeService.obtenerHistorial(user1, user2, page, size));
    }

    @GetMapping("/historial/global")
    public ResponseEntity<Page<Mensaje>> obtenerHistorialGlobal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/chat/historial/global - page={} size={}", page, size);
        return ResponseEntity.ok(mensajeService.obtenerHistorialGlobal(page, size));
    }



}
