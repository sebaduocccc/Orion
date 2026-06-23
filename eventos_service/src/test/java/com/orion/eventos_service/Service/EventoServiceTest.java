package com.orion.eventos_service.Service;

import com.orion.eventos_service.DTO.EventoMapper;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Entity.Evento;
import com.orion.eventos_service.Repository.RepositoryEvento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock private RepositoryEvento repo;
    @Mock private EventoMapper mapper;

    @InjectMocks private EventoService service;


    private EventoRequest buildRequest() {
        return EventoRequest.builder()
                .nombre("Hackathon")
                .lugar("Santiago")
                .fecha(LocalDateTime.now().plusDays(3))
                .build();
    }

    private EventoResponse buildResponse(Long id) {
        EventoResponse r = new EventoResponse();
        r.setIdEvento(id);
        r.setNombre("Hackathon");
        return r;
    }

    // =====================================================
    // guardar()
    // =====================================================
    @Test
    @DisplayName("guardar: mapea, persiste y devuelve la respuesta")
    void guardar_ok() {
        // given
        EventoRequest req = buildRequest();
        Long idCreador = 1L;

        Evento entidad = new Evento();
        Evento guardado = new Evento();
        guardado.setIdEvento(10L);
        EventoResponse esperado = buildResponse(10L);

        when(mapper.aEntidad(req, idCreador)).thenReturn(entidad);
        when(repo.save(entidad)).thenReturn(guardado);
        when(mapper.aResponse(guardado)).thenReturn(esperado);

        // when
        EventoResponse resultado = service.guardar(req, idCreador);

        // then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdEvento());
        verify(repo).save(entidad);
    }

    // =====================================================
    // obtenerTodos()
    // =====================================================
    @Test
    @DisplayName("obtenerTodos: devuelve la lista mapeada")
    void obtenerTodos_ok() {
        // given
        Evento e1 = new Evento(); e1.setIdEvento(1L);
        Evento e2 = new Evento(); e2.setIdEvento(2L);
        when(repo.findAll()).thenReturn(List.of(e1, e2));
        when(mapper.aResponse(e1)).thenReturn(buildResponse(1L));
        when(mapper.aResponse(e2)).thenReturn(buildResponse(2L));

        // when
        List<EventoResponse> resultado = service.obtenerTodos();

        // then
        assertEquals(2, resultado.size());
        verify(repo).findAll();
    }

    // =====================================================
    // obtenerPorId()
    // =====================================================
    @Test
    @DisplayName("obtenerPorId: devuelve el evento cuando existe")
    void obtenerPorId_ok() {
        // given
        Evento evento = new Evento();
        evento.setIdEvento(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(evento));
        when(mapper.aResponse(evento)).thenReturn(buildResponse(1L));

        // when
        EventoResponse resultado = service.obtenerPorId(1L);

        // then
        assertEquals(1L, resultado.getIdEvento());
    }

    @Test
    @DisplayName("obtenerPorId: lanza excepción si no existe")
    void obtenerPorId_noExiste() {
        // given
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(99L));
        verify(mapper, never()).aResponse(any());
    }

    // =====================================================
    // actualizar()
    // =====================================================
    @Test
    @DisplayName("actualizar: actualiza cuando el usuario es el creador")
    void actualizar_ok() {
        // given
        Evento existente = new Evento();
        existente.setIdEvento(5L);
        existente.setIdCreador(1L);
        when(repo.findById(5L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);
        when(mapper.aResponse(existente)).thenReturn(buildResponse(5L));

        EventoRequest req = buildRequest();

        // when
        EventoResponse resultado = service.actualizar(5L, req, 1L);

        // then
        assertNotNull(resultado);
        assertEquals("Hackathon", existente.getNombre()); // se aplicaron los cambios
        verify(repo).save(existente);
    }

    @Test
    @DisplayName("actualizar: lanza excepción si quien edita no es el creador (regla de negocio)")
    void actualizar_sinPermiso() {
        // given
        Evento existente = new Evento();
        existente.setIdEvento(5L);
        existente.setIdCreador(1L); // dueño = usuario 1
        when(repo.findById(5L)).thenReturn(Optional.of(existente));

        EventoRequest req = buildRequest();

        // when + then -> intenta editar el usuario 2
        assertThrows(RuntimeException.class, () -> service.actualizar(5L, req, 2L));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("actualizar: lanza excepción si el evento no existe")
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
        Evento existente = new Evento();
        existente.setIdEvento(9L);
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
        Evento existente = new Evento();
        existente.setIdEvento(9L);
        existente.setIdCreador(1L);
        when(repo.findById(9L)).thenReturn(Optional.of(existente));

        // when + then
        assertThrows(RuntimeException.class, () -> service.eliminar(9L, 2L));
        verify(repo, never()).deleteById(any());
    }

    // =====================================================
    // unirseAEvento()
    // =====================================================
    @Test
    @DisplayName("unirseAEvento: agrega al usuario a la lista de asistentes")
    void unirse_ok() {
        // given
        Evento evento = new Evento();
        evento.setIdEvento(7L);
        evento.setAsistentes(new ArrayList<>()); // lista mutable y vacía
        when(repo.findById(7L)).thenReturn(Optional.of(evento));
        when(repo.save(evento)).thenReturn(evento);
        when(mapper.aResponse(evento)).thenReturn(buildResponse(7L));

        // when
        service.unirseAEvento(7L, 5L);

        // then
        assertTrue(evento.getAsistentes().contains(5L)); // se inscribió
        verify(repo).save(evento);
    }

    @Test
    @DisplayName("unirseAEvento: lanza excepción si el usuario ya está inscrito (regla de negocio)")
    void unirse_duplicado() {
        // given
        Evento evento = new Evento();
        evento.setIdEvento(7L);
        evento.setAsistentes(new ArrayList<>(List.of(5L))); // ya está
        when(repo.findById(7L)).thenReturn(Optional.of(evento));

        // when + then
        assertThrows(RuntimeException.class, () -> service.unirseAEvento(7L, 5L));
        verify(repo, never()).save(any());
    }
}