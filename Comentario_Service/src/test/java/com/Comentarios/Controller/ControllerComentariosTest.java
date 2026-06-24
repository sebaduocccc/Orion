package com.Comentarios.Controller;

import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Service.ServiceComentario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerComentarioTest {

    @Mock private ServiceComentario service;
    @InjectMocks private ControllerComentario controller;

    private static final Long USER_ID = 5L;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(USER_ID, null));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private ResponseComentario buildResponse(Long id) {
        ResponseComentario r = new ResponseComentario();
        r.setId(id);
        r.setPostId(10L);
        r.setUserId(USER_ID);
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
    @DisplayName("comentar: 201 created con userId del JWT")
    void comentar_ok() {
        RequestComentario req = RequestComentario.builder().contenido("Buen post").build();
        when(service.guardar(10L, req, USER_ID)).thenReturn(buildResponse(1L));

        ResponseEntity<ResponseComentario> resp = controller.comentar(10L, req);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(1L, resp.getBody().getId());
        verify(service).guardar(10L, req, USER_ID);
    }

    @Test
    @DisplayName("actualizar: 200 ok con userId del JWT")
    void actualizar_ok() {
        RequestComentario req = RequestComentario.builder().contenido("Editado").build();
        when(service.actualizar(1L, req, USER_ID)).thenReturn(buildResponse(1L));

        ResponseEntity<ResponseComentario> resp = controller.actualizar(1L, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(service).actualizar(1L, req, USER_ID);
    }
}
