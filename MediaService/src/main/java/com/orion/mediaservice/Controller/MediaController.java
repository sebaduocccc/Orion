package com.orion.mediaservice.Controller;

import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Service.StorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/media")
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    @Autowired
    private StorageService storageService;


    @PostMapping("/avatar/upload")
    public ResponseEntity<Media> subirAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) {
            log.warn("POST /api/media/avatar/upload - Acceso denegado: X-Auth-User-Id ausente");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("POST /api/media/avatar/upload - Usuario id={}", userId);
        Media mediaGuardada = storageService.guardarArchivo(file, userId, Media.TipoMedia.AVATAR);
        log.info("Avatar subido para usuario id={}: {}", userId, mediaGuardada.getNombreGenerado());
        return ResponseEntity.ok(mediaGuardada);
    }


    @GetMapping("/avatar/{nombreGenerado}")
    public ResponseEntity<Resource> verAvatar(@PathVariable String nombreGenerado) {
        log.info("GET /api/media/avatar/{}", nombreGenerado);
        Resource file = storageService.cargarArchivo(nombreGenerado, Media.TipoMedia.AVATAR);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }


    @PostMapping("/post/upload")
    public ResponseEntity<Media> subirFotoPost(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-Auth-User-Id", required = false) Long userId) {
        if (userId == null) {
            log.warn("POST /api/media/post/upload - Acceso denegado: X-Auth-User-Id ausente");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("POST /api/media/post/upload - Usuario id={}", userId);
        Media mediaGuardada = storageService.guardarArchivo(file, userId, Media.TipoMedia.POST);
        log.info("Imagen de post subida para usuario id={}: {}", userId, mediaGuardada.getNombreGenerado());
        return ResponseEntity.ok(mediaGuardada);
    }


    @GetMapping("/post/{nombreGenerado}")
    public ResponseEntity<Resource> verFotoPost(@PathVariable String nombreGenerado) {
        log.info("GET /api/media/post/{}", nombreGenerado);
        Resource file = storageService.cargarArchivo(nombreGenerado, Media.TipoMedia.POST);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }
}
