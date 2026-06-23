package com.Comentarios.Controller;

import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Service.ServiceComentario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerComentarioTest {

    @Mock private ServiceComentario service;
    @InjectMocks private ControllerComentario controller;

    private ResponseComentario buildResponse(Long id) {
        ResponseComentario r = new ResponseComentario();
        r.setId(id);
        r.setPostId(10L);
        r.setUserId(5L);
        r.setContenido("Buen post");
        return r;
    }

    @Test
    @DisplayName("comentariosPost: 200 con lista")
    void comentariosPost_ok() {
        when(service.buscarPorPost(10L)).thenReturn(List.of(buildResponse(1L)));

        ResponseEntity<List<ResponseComentario>> resp = controller.comentariosPost(10L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    @DisplayName("comentar: 201 created")
    void comentar_ok() {
        RequestComentario req = RequestComentario.builder()
                .postId(10L).userId(5L).contenido("Buen post").build();
        when(service.guardar(10L, req)).thenReturn(buildResponse(1L));

        ResponseEntity<ResponseComentario> resp = controller.comentar(10L, req);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(1L, resp.getBody().getId());
    }

    @Test
    @DisplayName("actualizar: 200 ok")
    void actualizar_ok() {
        RequestComentario req = RequestComentario.builder()
                .postId(10L).userId(5L).contenido("Editado").build();
        when(service.actualizar(1L, req)).thenReturn(buildResponse(1L));

        ResponseEntity<ResponseComentario> resp = controller.actualizar(1L, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(service).actualizar(1L, req);
    }
}