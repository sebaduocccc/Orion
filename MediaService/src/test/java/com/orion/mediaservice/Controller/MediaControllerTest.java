package com.orion.mediaservice.Controller;

import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Service.StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    @Mock private StorageService storageService;
    @Mock private MultipartFile file;
    @InjectMocks private MediaController controller;

    @Test
    @DisplayName("subirAvatar: sin usuario autenticado retorna 401")
    void subirAvatar_sinUsuario() {
        ResponseEntity<Media> resp = controller.subirAvatar(file, null);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        verifyNoInteractions(storageService);
    }

    @Test
    @DisplayName("subirAvatar: con usuario autenticado guarda y retorna 200")
    void subirAvatar_ok() {
        Media media = mock(Media.class);
        when(storageService.guardarArchivo(file, 5L, Media.TipoMedia.AVATAR)).thenReturn(media);

        ResponseEntity<Media> resp = controller.subirAvatar(file, 5L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(media, resp.getBody());
    }

    @Test
    @DisplayName("subirFotoPost: sin usuario autenticado retorna 401")
    void subirFotoPost_sinUsuario() {
        ResponseEntity<Media> resp = controller.subirFotoPost(file, null);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        verifyNoInteractions(storageService);
    }

    @Test
    @DisplayName("subirFotoPost: con usuario autenticado guarda y retorna 200")
    void subirFotoPost_ok() {
        Media media = mock(Media.class);
        when(storageService.guardarArchivo(file, 5L, Media.TipoMedia.POST)).thenReturn(media);

        ResponseEntity<Media> resp = controller.subirFotoPost(file, 5L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(media, resp.getBody());
    }

    @Test
    @DisplayName("verAvatar: retorna el archivo con 200")
    void verAvatar_ok() {
        Resource recurso = mock(Resource.class);
        when(recurso.getFilename()).thenReturn("avatar.jpg");
        when(storageService.cargarArchivo("avatar.jpg", Media.TipoMedia.AVATAR)).thenReturn(recurso);

        ResponseEntity<Resource> resp = controller.verAvatar("avatar.jpg");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(recurso, resp.getBody());
    }

    @Test
    @DisplayName("verFotoPost: retorna el archivo con 200")
    void verFotoPost_ok() {
        Resource recurso = mock(Resource.class);
        when(recurso.getFilename()).thenReturn("post.jpg");
        when(storageService.cargarArchivo("post.jpg", Media.TipoMedia.POST)).thenReturn(recurso);

        ResponseEntity<Resource> resp = controller.verFotoPost("post.jpg");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(recurso, resp.getBody());
    }
}
