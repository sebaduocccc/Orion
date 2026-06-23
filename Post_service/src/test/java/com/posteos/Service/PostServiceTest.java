package com.posteos.Service;

import com.posteos.DTO.PostMapper;
import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Exception.ResourceNotFoundException;
import com.posteos.Repository.Repository_Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private Repository_Post repo;
    @Mock private PostMapper mapper;
    @InjectMocks private ServicePost service;

    private PostRequestDTO buildRequest() {
        return PostRequestDTO.builder()
                .userId(5L).content("Hola mundo").mediaUrl("img.png").build();
    }

    private Post buildEntity(Long id) {
        Post p = new Post();
        p.setId(id);
        p.setUserId(5L);
        p.setContent("Hola mundo");
        p.setMediaUrl("img.png");
        p.setCreadoEl(LocalDateTime.now());
        return p;
    }

    private PostResponseDTO buildResponse(Long id) {
        return new PostResponseDTO(id, 5L, "Hola mundo", "img.png", LocalDateTime.now());
    }

    @Test
    @DisplayName("guardar: persiste y retorna response")
    void guardar_ok() {
        PostRequestDTO req = buildRequest();
        Post entidad = buildEntity(null);
        Post guardado = buildEntity(1L);

        when(mapper.aEntidad(req)).thenReturn(entidad);
        when(repo.save(entidad)).thenReturn(guardado);
        when(mapper.response(guardado)).thenReturn(buildResponse(1L));

        PostResponseDTO result = service.guardar(req);

        assertEquals(1L, result.getId());
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("buscarPorId: existente retorna response")
    void buscarPorId_ok() {
        Post p = buildEntity(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(mapper.response(p)).thenReturn(buildResponse(1L));

        assertEquals(1L, service.buscarPorId(1L).getId());
    }

    @Test
    @DisplayName("buscarPorId: no existente lanza excepción")
    void buscarPorId_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista mapeada")
    void obtenerTodos_ok() {
        Post p = buildEntity(1L);
        when(repo.findAll()).thenReturn(List.of(p));
        when(mapper.response(p)).thenReturn(buildResponse(1L));

        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("buscarPostDeUsuario: arma DTO manualmente")
    void buscarPostDeUsuario_ok() {
        when(repo.findByUserIdOrderByCreadoElDesc(5L)).thenReturn(List.of(buildEntity(1L)));

        List<PostResponseDTO> result = service.buscarPostDeUsuario(5L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getUserId());
    }

    @Test
    @DisplayName("buscarUser: usa mapper")
    void buscarUser_ok() {
        Post p = buildEntity(1L);
        when(repo.findByUserIdOrderByCreadoElDesc(5L)).thenReturn(List.of(p));
        when(mapper.response(p)).thenReturn(buildResponse(1L));

        assertEquals(1, service.buscarUser(5L).size());
    }

    @Test
    @DisplayName("totalDePostDeUsuario: retorna conteo")
    void total_ok() {
        when(repo.countByUserId(5L)).thenReturn(3L);
        assertEquals(3L, service.totalDePostDeUsuario(5L));
    }

    @Test
    @DisplayName("actualizar: existente modifica campos")
    void actualizar_ok() {
        Post existente = buildEntity(1L);
        PostRequestDTO req = buildRequest();
        req.setContent("Editado");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);
        when(mapper.response(existente)).thenReturn(buildResponse(1L));

        service.actualizar(1L, req);

        assertEquals("Editado", existente.getContent());
    }

    @Test
    @DisplayName("actualizar: no existente lanza excepción")
    void actualizar_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.actualizar(99L, buildRequest()));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("eliminar: existente borra")
    void eliminar_ok() {
        when(repo.findById(1L)).thenReturn(Optional.of(buildEntity(1L)));
        service.eliminar(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar: no existente lanza excepción")
    void eliminar_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repo, never()).deleteById(any());
    }

    @Test
    @DisplayName("cargarFeedPrincipal: retorna page")
    void feedPrincipal_ok() {
        Page<Post> page = new PageImpl<>(List.of(buildEntity(1L)));
        when(repo.findAllByOrderByCreadoElDesc(any())).thenReturn(page);

        assertEquals(1, service.cargarFeedPrincipal(0, 10).getTotalElements());
    }

    @Test
    @DisplayName("cargarFeedUsuario: retorna page")
    void feedUsuario_ok() {
        Page<Post> page = new PageImpl<>(List.of(buildEntity(1L)));
        when(repo.findByUserIdOrderByCreadoElDesc(eq(5L), any())).thenReturn(page);

        assertEquals(1, service.cargarFeedUsuario(0, 10, 5L).getTotalElements());
    }
}