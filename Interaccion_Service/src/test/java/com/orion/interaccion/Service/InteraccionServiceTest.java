package com.orion.interaccion.Service;

import com.orion.interaccion.Entity.Like;
import com.orion.interaccion.Repository.LikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteraccionServiceTest {

    @Mock private LikeRepository repo;
    @InjectMocks private InteraccionService service;

    private Like buildLike(Long usuarioId, Long postId) {
        Like like = new Like();
        like.setUsuarioId(usuarioId);
        like.setPostId(postId);
        return like;
    }

    @Test
    @DisplayName("toggleLike: si no existe like previo, lo crea y retorna true")
    void toggleLike_creaLike() {
        // Given
        when(repo.findByUsuarioIdAndPostId(1L, 10L)).thenReturn(Optional.empty());

        // When
        boolean resultado = service.toggleLike(1L, 10L);

        // Then
        assertTrue(resultado);
        verify(repo).save(any(Like.class));
    }

    @Test
    @DisplayName("toggleLike: si ya existe like, lo elimina y retorna false")
    void toggleLike_eliminaLike() {
        Like existente = buildLike(1L, 10L);
        when(repo.findByUsuarioIdAndPostId(1L, 10L)).thenReturn(Optional.of(existente));

        boolean resultado = service.toggleLike(1L, 10L);

        assertFalse(resultado);
        verify(repo).delete(existente);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("obtenerTotalLikes: retorna el conteo del repositorio")
    void obtenerTotalLikes_ok() {
        when(repo.countByPostId(10L)).thenReturn(7L);

        assertEquals(7L, service.obtenerTotalLikes(10L));
    }

    @Test
    @DisplayName("verificarSiLikeo: true cuando existe el like")
    void verificarSiLikeo_true() {
        when(repo.findByUsuarioIdAndPostId(1L, 10L)).thenReturn(Optional.of(buildLike(1L, 10L)));

        assertTrue(service.verificarSiLikeo(1L, 10L));
    }

    @Test
    @DisplayName("verificarSiLikeo: false cuando no existe el like")
    void verificarSiLikeo_false() {
        when(repo.findByUsuarioIdAndPostId(1L, 10L)).thenReturn(Optional.empty());

        assertFalse(service.verificarSiLikeo(1L, 10L));
    }
}
