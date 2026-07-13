package com.orion.chatservice.Service;

import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Repository.MensajeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeServiceTest {

    @Mock private MensajeRepository repo;
    @InjectMocks private MensajeService service;

    private Mensaje buildMensaje(Long senderId, Long receiverId) {
        Mensaje m = new Mensaje();
        m.setSenderId(senderId);
        m.setReceiverId(receiverId);
        m.setNombreEmisor("usuario" + senderId);
        m.setContenido("hola");
        return m;
    }

    @Test
    @DisplayName("guardarMensajePrivado: guarda cuando el receptor es válido")
    void guardarPrivado_ok() {
        Mensaje mensaje = buildMensaje(1L, 2L);
        when(repo.save(mensaje)).thenReturn(mensaje);

        Mensaje guardado = service.guardarMensajePrivado(mensaje);

        assertEquals(2L, guardado.getReceiverId());
        verify(repo).save(mensaje);
    }

    @Test
    @DisplayName("guardarMensajePrivado: rechaza receptor nulo")
    void guardarPrivado_receptorNulo() {
        Mensaje mensaje = buildMensaje(1L, null);

        assertThrows(IllegalArgumentException.class, () -> service.guardarMensajePrivado(mensaje));
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("guardarMensajePrivado: rechaza el receptor del canal global")
    void guardarPrivado_receptorGlobal() {
        Mensaje mensaje = buildMensaje(1L, 0L);

        assertThrows(IllegalArgumentException.class, () -> service.guardarMensajePrivado(mensaje));
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("guardarMensajeGlobal: fuerza receiverId=0 y guarda")
    void guardarGlobal_ok() {
        Mensaje mensaje = buildMensaje(1L, 99L);
        when(repo.save(mensaje)).thenReturn(mensaje);

        Mensaje guardado = service.guardarMensajeGlobal(mensaje);

        assertEquals(MensajeService.RECEIVER_GLOBAL, guardado.getReceiverId());
        verify(repo).save(mensaje);
    }

    @Test
    @DisplayName("obtenerHistorial: retorna la página del repositorio")
    void obtenerHistorial_ok() {
        Page<Mensaje> pagina = new PageImpl<>(List.of(buildMensaje(1L, 2L)));
        when(repo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByFechaEnvioDesc(
                eq(1L), eq(2L), eq(2L), eq(1L), any(Pageable.class))).thenReturn(pagina);

        Page<Mensaje> resultado = service.obtenerHistorial(1L, 2L, 0, 10);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    @DisplayName("obtenerHistorialGlobal: consulta los mensajes del canal global")
    void obtenerHistorialGlobal_ok() {
        Page<Mensaje> pagina = new PageImpl<>(List.of(buildMensaje(1L, 0L), buildMensaje(2L, 0L)));
        when(repo.findByReceiverIdOrderByFechaEnvioDesc(eq(0L), any(Pageable.class))).thenReturn(pagina);

        Page<Mensaje> resultado = service.obtenerHistorialGlobal(0, 20);

        assertEquals(2, resultado.getTotalElements());
    }
}
