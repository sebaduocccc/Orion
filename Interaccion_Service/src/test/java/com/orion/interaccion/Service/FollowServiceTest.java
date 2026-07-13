package com.orion.interaccion.Service;

import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Repository.FollowRepository;
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
class FollowServiceTest {

    @Mock private FollowRepository repo;
    @InjectMocks private FollowService service;

    private Follow buildFollow(Long seguidorId, Long seguidoId) {
        Follow follow = new Follow();
        follow.setSeguidorId(seguidorId);
        follow.setSeguidoId(seguidoId);
        return follow;
    }

    @Test
    @DisplayName("toggleFollow: rechaza el auto-seguimiento")
    void toggleFollow_autoSeguimiento() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.toggleFollow(1L, 1L));

        assertTrue(ex.getMessage().contains("seguirte a ti mismo"));
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("toggleFollow: si no seguía, crea el follow y retorna true")
    void toggleFollow_creaFollow() {
        when(repo.findBySeguidorIdAndSeguidoId(1L, 2L)).thenReturn(Optional.empty());

        boolean resultado = service.toggleFollow(1L, 2L);

        assertTrue(resultado);
        verify(repo).save(any(Follow.class));
    }

    @Test
    @DisplayName("toggleFollow: si ya seguía, elimina el follow y retorna false")
    void toggleFollow_eliminaFollow() {
        Follow existente = buildFollow(1L, 2L);
        when(repo.findBySeguidorIdAndSeguidoId(1L, 2L)).thenReturn(Optional.of(existente));

        boolean resultado = service.toggleFollow(1L, 2L);

        assertFalse(resultado);
        verify(repo).delete(existente);
    }

    @Test
    @DisplayName("obtenerContadorSeguidores: retorna el conteo del repositorio")
    void contadorSeguidores_ok() {
        when(repo.countBySeguidoId(5L)).thenReturn(3L);

        assertEquals(3L, service.obtenerContadorSeguidores(5L));
    }

    @Test
    @DisplayName("obtenerContadorSeguidos: retorna el conteo del repositorio")
    void contadorSeguidos_ok() {
        when(repo.countBySeguidorId(5L)).thenReturn(4L);

        assertEquals(4L, service.obtenerContadorSeguidos(5L));
    }

    @Test
    @DisplayName("verificarSiSigue: true cuando existe la relación")
    void verificarSiSigue_true() {
        when(repo.findBySeguidorIdAndSeguidoId(1L, 2L)).thenReturn(Optional.of(buildFollow(1L, 2L)));

        assertTrue(service.verificarSiSigue(1L, 2L));
    }
}
