package com.posteos.Controller;

import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Service.ServicePost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock private ServicePost service;
    @InjectMocks private ControllerPost controller;

    private PostResponseDTO buildResponse(Long id) {
        return new PostResponseDTO(id, 5L, "Hola mundo", "img.png", LocalDateTime.now());
    }

    private PostRequestDTO buildRequest() {
        return PostRequestDTO.builder().content("Hola mundo").mediaUrl("img.png").build();
    }

    @Test
    @DisplayName("guardar: sin userId retorna 401")
    void guardar_sinUser() {
        ResponseEntity<?> resp = controller.guardar(buildRequest(), null);
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    @DisplayName("guardar: con userId retorna 201")
    void guardar_ok() {
        when(service.guardar(any())).thenReturn(buildResponse(1L));

        ResponseEntity<?> resp = controller.guardar(buildRequest(), 5L);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    }

    @Test
    @DisplayName("obtenerTodos: con datos 200")
    void obtenerTodos_ok() {
        when(service.obtenerTodos()).thenReturn(List.of(buildResponse(1L)));
        assertEquals(HttpStatus.OK, controller.obtenerTodos().getStatusCode());
    }

    @Test
    @DisplayName("obtenerTodos: vacío 204")
    void obtenerTodos_vacio() {
        when(service.obtenerTodos()).thenReturn(Collections.emptyList());
        assertEquals(HttpStatus.NO_CONTENT, controller.obtenerTodos().getStatusCode());
    }

    @Test
    @DisplayName("obtenerTodosPorUsuario: con datos 200")
    void porUsuario_ok() {
        when(service.buscarPostDeUsuario(5L)).thenReturn(List.of(buildResponse(1L)));
        assertEquals(HttpStatus.OK, controller.obtenerTodosPorUsuario(5L).getStatusCode());
    }

    @Test
    @DisplayName("obtenerTodosPorUsuario: vacío 404")
    void porUsuario_vacio() {
        when(service.buscarPostDeUsuario(5L)).thenReturn(Collections.emptyList());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerTodosPorUsuario(5L).getStatusCode());
    }

    @Test
    @DisplayName("obtenerCount: 200 con total")
    void count_ok() {
        when(service.totalDePostDeUsuario(5L)).thenReturn(4L);
        ResponseEntity<Long> resp = controller.obtenerCountLikesUsuario(5L);
        assertEquals(4L, resp.getBody());
    }

    @Test
    @DisplayName("obtenerPorId: 200")
    void porId_ok() {
        when(service.buscarPorId(1L)).thenReturn(buildResponse(1L));
        assertEquals(HttpStatus.OK, controller.obtenerPorId(1L).getStatusCode());
    }

    @Test
    @DisplayName("verPostsUser: con datos 200")
    void verPostsUser_ok() {
        when(service.buscarUser(5L)).thenReturn(List.of(buildResponse(1L)));
        assertEquals(HttpStatus.OK, controller.verPostsUser(5L).getStatusCode());
    }

    @Test
    @DisplayName("verPostsUser: vacío 404")
    void verPostsUser_vacio() {
        when(service.buscarUser(5L)).thenReturn(Collections.emptyList());
        assertEquals(HttpStatus.NOT_FOUND, controller.verPostsUser(5L).getStatusCode());
    }

    @Test
    @DisplayName("actualizar: 200")
    void actualizar_ok() {
        when(service.actualizar(eq(1L), any())).thenReturn(buildResponse(1L));
        assertEquals(HttpStatus.OK, controller.actualizar(1L, buildRequest()).getStatusCode());
    }

    @Test
    @DisplayName("borrar: 200")
    void borrar_ok() {
        doNothing().when(service).eliminar(1L);
        assertEquals(HttpStatus.OK, controller.borrar(1L).getStatusCode());
        verify(service).eliminar(1L);
    }

    @Test
    @DisplayName("obtenerTodosFeed: retorna page")
    void feed_ok() {
        Post p = new Post();
        Page<Post> page = new PageImpl<>(List.of(p));
        when(service.cargarFeedPrincipal(0, 10)).thenReturn(page);
        assertEquals(1, controller.obtenerTodosFeed(0, 10).getTotalElements());
    }

    @Test
    @DisplayName("obtenerFeedUsuario: retorna page")
    void feedUsuario_ok() {
        Page<Post> page = new PageImpl<>(List.of(new Post()));
        when(service.cargarFeedUsuario(0, 10, 5L)).thenReturn(page);
        assertEquals(1, controller.obtenerFeedUsuario(0, 10, 5L).getTotalElements());
    }
}