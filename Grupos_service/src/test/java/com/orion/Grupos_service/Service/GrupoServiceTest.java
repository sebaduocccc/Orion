package com.orion.Grupos_service.Service;

import com.orion.Grupos_service.Dto.MapperGrupo;
import com.orion.Grupos_service.Dto.RequestGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import com.orion.Grupos_service.Entity.Grupo;
import com.orion.Grupos_service.Repository.Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoServiceTest {

    @Mock private Repository repo;
    @Mock private MapperGrupo mapper;

    @InjectMocks private ServiceGrupo service;

    // ---------- helpers ----------
    private RequestGrupo buildRequest() {
        RequestGrupo req = new RequestGrupo();
        req.setNombre("Devs Duoc");
        req.setDescripcion("Grupo de programación");
        return req;
    }

    private ResponseGrupo buildResponse(Long id) {
        ResponseGrupo r = new ResponseGrupo();
        r.setIdGrupo(id);
        r.setNombre("Devs Duoc");
        return r;
    }

    // =====================================================
    // guardar()
    // =====================================================
    @Test
    @DisplayName("guardar: mapea, persiste y devuelve la respuesta")
    void guardar_ok() {
        // given
        RequestGrupo req = buildRequest();
        Long idCreador = 1L;

        Grupo entidad = new Grupo();
        Grupo guardado = new Grupo();
        ResponseGrupo esperado = buildResponse(10L);

        when(mapper.toEntity(req, idCreador)).thenReturn(entidad);
        when(repo.save(entidad)).thenReturn(guardado);
        when(mapper.toResponse(guardado)).thenReturn(esperado);

        // when
        ResponseGrupo resultado = service.guardar(req, idCreador);

        // then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdGrupo());
        verify(repo).save(entidad);
    }

    // =====================================================
    // obtenerTodos()
    // =====================================================
    @Test
    @DisplayName("obtenerTodos: devuelve la lista mapeada")
    void obtenerTodos_ok() {
        // given
        Grupo g1 = new Grupo();
        Grupo g2 = new Grupo();
        when(repo.findAll()).thenReturn(List.of(g1, g2));
        when(mapper.toResponse(g1)).thenReturn(buildResponse(1L));
        when(mapper.toResponse(g2)).thenReturn(buildResponse(2L));

        // when
        List<ResponseGrupo> resultado = service.obtenerTodos();

        // then
        assertEquals(2, resultado.size());
        verify(repo).findAll();
    }

    // =====================================================
    // obtenerPorId()
    // =====================================================
    @Test
    @DisplayName("obtenerPorId: devuelve el grupo cuando existe")
    void obtenerPorId_ok() {
        // given
        Grupo grupo = new Grupo();
        when(repo.findById(1L)).thenReturn(Optional.of(grupo));
        when(mapper.toResponse(grupo)).thenReturn(buildResponse(1L));

        // when
        ResponseGrupo resultado = service.obtenerPorId(1L);

        // then
        assertEquals(1L, resultado.getIdGrupo());
    }

    @Test
    @DisplayName("obtenerPorId: lanza excepción si no existe")
    void obtenerPorId_noExiste() {
        // given
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(99L));
        verify(mapper, never()).toResponse(any());
    }

    // =====================================================
    // actualizar()
    // =====================================================
    @Test
    @DisplayName("actualizar: actualiza cuando el usuario es el creador")
    void actualizar_ok() {
        // given
        Grupo existente = new Grupo();
        existente.setIdCreador(1L);
        when(repo.findById(5L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);
        when(mapper.toResponse(existente)).thenReturn(buildResponse(5L));

        // when
        ResponseGrupo resultado = service.actualizar(5L, buildRequest(), 1L);

        // then
        assertNotNull(resultado);
        assertEquals("Devs Duoc", existente.getNombre()); // se aplicaron los cambios
        verify(repo).save(existente);
    }

    @Test
    @DisplayName("actualizar: lanza excepción si quien edita no es el creador (regla de negocio)")
    void actualizar_sinPermiso() {
        // given
        Grupo existente = new Grupo();
        existente.setIdCreador(1L); // dueño = usuario 1
        when(repo.findById(5L)).thenReturn(Optional.of(existente));

        // when + then -> intenta editar el usuario 2
        assertThrows(RuntimeException.class, () -> service.actualizar(5L, buildRequest(), 2L));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el grupo no existe")
    void actualizar_noExiste() {
        // given
        when(repo.findById(404L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(RuntimeException.class, () -> service.actualizar(404L, buildRequest(), 1L));
        verify(repo, never()).save(any());
    }

    // =====================================================
    // eliminar()
    // =====================================================
    @Test
    @DisplayName("eliminar: borra cuando el usuario es el creador")
    void eliminar_ok() {
        // given
        Grupo existente = new Grupo();
        existente.setIdCreador(1L);
        when(repo.findById(9L)).thenReturn(Optional.of(existente));

        // when
        service.eliminar(9L, 1L);

        // then
        verify(repo).deleteById(9L);
    }

    @Test
    @DisplayName("eliminar: lanza excepción si quien borra no es el creador (regla de negocio)")
    void eliminar_sinPermiso() {
        // given
        Grupo existente = new Grupo();
        existente.setIdCreador(1L);
        when(repo.findById(9L)).thenReturn(Optional.of(existente));

        // when + then
        assertThrows(RuntimeException.class, () -> service.eliminar(9L, 2L));
        verify(repo, never()).deleteById(any());
    }

    // =====================================================
    // unirseAGrupo()
    // =====================================================
    @Test
    @DisplayName("unirseAGrupo: agrega al usuario a la lista de miembros")
    void unirse_ok() {
        // given
        Grupo grupo = new Grupo();
        grupo.setMiembros(new ArrayList<>()); // lista mutable y vacía
        when(repo.findById(7L)).thenReturn(Optional.of(grupo));
        when(repo.save(grupo)).thenReturn(grupo);
        when(mapper.toResponse(grupo)).thenReturn(buildResponse(7L));

        // when
        service.unirseAGrupo(7L, 5L);

        // then
        assertTrue(grupo.getMiembros().contains(5L)); // se inscribió
        verify(repo).save(grupo);
    }

    @Test
    @DisplayName("unirseAGrupo: lanza excepción si el usuario ya está inscrito (regla de negocio)")
    void unirse_duplicado() {
        // given
        Grupo grupo = new Grupo();
        grupo.setMiembros(new ArrayList<>(List.of(5L))); // ya está
        when(repo.findById(7L)).thenReturn(Optional.of(grupo));

        // when + then
        assertThrows(RuntimeException.class, () -> service.unirseAGrupo(7L, 5L));
        verify(repo, never()).save(any());
    }
}