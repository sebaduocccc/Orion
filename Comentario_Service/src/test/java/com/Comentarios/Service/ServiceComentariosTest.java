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

    private RequestComentario buildRequest() {
        return RequestComentario.builder()
                .postId(10L).userId(5L).contenido("Buen post").build();
    }

    private Comentario buildEntity(Long id) {
        Comentario c = new Comentario();
        c.setId(id);
        c.setPostId(10L);
        c.setUserId(5L);
        c.setContenido("Buen post");
        c.setCreadoEl(LocalDateTime.now());
        return c;
    }

    private ResponseComentario buildResponse(Long id) {
        ResponseComentario r = new ResponseComentario();
        r.setId(id);
        r.setPostId(10L);
        r.setUserId(5L);
        r.setContenido("Buen post");
        r.setCreadoEl(LocalDateTime.now());
        return r;
    }

    @Test
    @DisplayName("guardar: asigna postId, guarda y retorna response")
    void guardar_ok() {
        RequestComentario req = buildRequest();
        Comentario entidad = buildEntity(null);
        Comentario guardado = buildEntity(1L);
        ResponseComentario resp = buildResponse(1L);

        when(mapper.aEntidad(req)).thenReturn(entidad);
        when(repo.save(entidad)).thenReturn(guardado);
        when(mapper.response(guardado)).thenReturn(resp);

        ResponseComentario result = service.guardar(99L, req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(99L, entidad.getPostId());
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("actualizar: existente modifica contenido")
    void actualizar_ok() {
        Comentario existente = buildEntity(1L);
        RequestComentario req = buildRequest();
        req.setContenido("Editado");
        ResponseComentario resp = buildResponse(1L);
        resp.setContenido("Editado");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);
        when(mapper.response(existente)).thenReturn(resp);

        ResponseComentario result = service.actualizar(1L, req);

        assertEquals("Editado", existente.getContenido());
        assertEquals("Editado", result.getContenido());
    }

    @Test
    @DisplayName("actualizar: no existente lanza ResourceNotFoundException")
    void actualizar_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.actualizar(99L, buildRequest()));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("buscarPorPost: retorna lista mapeada")
    void buscarPorPost_ok() {
        Comentario c = buildEntity(1L);
        when(repo.findByPostId(10L)).thenReturn(List.of(c));
        when(mapper.response(c)).thenReturn(buildResponse(1L));

        List<ResponseComentario> result = service.buscarPorPost(10L);

        assertEquals(1, result.size());
        verify(repo).findByPostId(10L);
    }

    @Test
    @DisplayName("buscarPorUsuario: retorna lista mapeada")
    void buscarPorUsuario_ok() {
        Comentario c = buildEntity(1L);
        when(repo.findByUserId(5L)).thenReturn(List.of(c));
        when(mapper.response(c)).thenReturn(buildResponse(1L));

        List<ResponseComentario> result = service.buscarPorUsuario(5L);

        assertEquals(1, result.size());
        verify(repo).findByUserId(5L);
    }

    @Test
    @DisplayName("contarcomentarios: retorna conteo")
    void contar_ok() {
        when(repo.countByPostId(10L)).thenReturn(7L);
        assertEquals(7L, service.contarcomentarios(10L));
    }
}