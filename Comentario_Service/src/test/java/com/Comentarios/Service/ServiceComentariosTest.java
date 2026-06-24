package com.Comentarios.Service;

import com.Comentarios.DTO.ComentarioMapper;
import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Entity.Comentario;
import com.Comentarios.Exceptions.ResourceNotFoundException;
import com.Comentarios.Repository.RepositoryComentario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceComentariosTest {

    @Mock private RepositoryComentario repo;
    @Mock private ComentarioMapper mapper;
    @InjectMocks private ServiceComentario service;

    private static final Long POST_ID = 10L;
    private static final Long USER_ID = 5L;

    private RequestComentario buildRequest() {
        return RequestComentario.builder().contenido("Buen post").build();
    }

    private Comentario buildEntity(Long id) {
        Comentario c = new Comentario();
        c.setId(id);
        c.setPostId(POST_ID);
        c.setUserId(USER_ID);
        c.setContenido("Buen post");
        c.setCreadoEl(LocalDateTime.now());
        return c;
    }

    private ResponseComentario buildResponse(Long id) {
        ResponseComentario r = new ResponseComentario();
        r.setId(id);
        r.setPostId(POST_ID);
        r.setUserId(USER_ID);
        r.setContenido("Buen post");
        r.setCreadoEl(LocalDateTime.now());
        return r;
    }

    @Test
    @DisplayName("guardar: asigna postId y userId, guarda y retorna response")
    void guardar_ok() {
        RequestComentario req = buildRequest();
        Comentario entidad = buildEntity(null);
        Comentario guardado = buildEntity(1L);
        ResponseComentario resp = buildResponse(1L);

        when(mapper.aEntidad(req, POST_ID, USER_ID)).thenReturn(entidad);
        when(repo.save(entidad)).thenReturn(guardado);
        when(mapper.response(guardado)).thenReturn(resp);

        ResponseComentario result = service.guardar(POST_ID, req, USER_ID);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("actualizar: existente y mismo usuario modifica contenido")
    void actualizar_ok() {
        Comentario existente = buildEntity(1L);
        RequestComentario req = buildRequest();
        req.setContenido("Editado");
        ResponseComentario resp = buildResponse(1L);
        resp.setContenido("Editado");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);
        when(mapper.response(existente)).thenReturn(resp);

        ResponseComentario result = service.actualizar(1L, req, USER_ID);

        assertEquals("Editado", existente.getContenido());
        assertEquals("Editado", result.getContenido());
    }

    @Test
    @DisplayName("actualizar: usuario diferente lanza excepción de permiso")
    void actualizar_sinPermiso() {
        Comentario existente = buildEntity(1L); // userId = 5L
        when(repo.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(RuntimeException.class, () -> service.actualizar(1L, buildRequest(), 99L));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("actualizar: no existente lanza ResourceNotFoundException")
    void actualizar_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.actualizar(99L, buildRequest(), USER_ID));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("buscarPorPost: retorna lista mapeada")
    void buscarPorPost_ok() {
        Comentario c = buildEntity(1L);
        when(repo.findByPostId(POST_ID)).thenReturn(List.of(c));
        when(mapper.response(c)).thenReturn(buildResponse(1L));

        List<ResponseComentario> result = service.buscarPorPost(POST_ID);

        assertEquals(1, result.size());
        verify(repo).findByPostId(POST_ID);
    }

    @Test
    @DisplayName("buscarPorUsuario: retorna lista mapeada")
    void buscarPorUsuario_ok() {
        Comentario c = buildEntity(1L);
        when(repo.findByUserId(USER_ID)).thenReturn(List.of(c));
        when(mapper.response(c)).thenReturn(buildResponse(1L));

        List<ResponseComentario> result = service.buscarPorUsuario(USER_ID);

        assertEquals(1, result.size());
        verify(repo).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("contarcomentarios: retorna conteo")
    void contar_ok() {
        when(repo.countByPostId(POST_ID)).thenReturn(7L);
        assertEquals(7L, service.contarcomentarios(POST_ID));
    }
}
