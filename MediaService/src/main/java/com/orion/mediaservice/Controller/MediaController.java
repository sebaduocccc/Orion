package com.orion.mediaservice.Controller;


import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Service.StorageService;
import org.springframework.core.io.Resource;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private StorageService storageService;

    // endpoints para avatares
    @PostMapping("/avatar/upload")
    public ResponseEntity<Media> subirAvatar(@RequestParam("file") MultipartFile file){
//        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        Long userId = 1L;
        Media mediaGuardada = storageService.guardarArchivo(file,userId,Media.TipoMedia.AVATAR);
        return ResponseEntity.ok(mediaGuardada);
    }


    @GetMapping("/avatar/{nombreGenerado}")
    public ResponseEntity<Resource> verAvatar(@PathVariable String nombreGenerado){
        Resource file = storageService.cargarArchivo(nombreGenerado,Media.TipoMedia.AVATAR);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);

    }


    // Endpoints para posts

    @PostMapping("/post/upload")
    public ResponseEntity<Media> subirFotoPost(@RequestParam("file") MultipartFile file){

//        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        Long userId = 1L;
        Media mediaGuardada = storageService.guardarArchivo(file,userId,Media.TipoMedia.POST);
        return ResponseEntity.ok(mediaGuardada);

    }


    @GetMapping("/post/{nombreGenerado}")
    public ResponseEntity<Resource> verFotoPost(@PathVariable String nombreGenerado){

        Resource file = storageService.cargarArchivo(nombreGenerado,Media.TipoMedia.POST);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);

    }


}
