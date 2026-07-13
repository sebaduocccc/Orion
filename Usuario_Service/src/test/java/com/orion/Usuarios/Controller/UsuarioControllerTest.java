package com.orion.Usuarios.Controller;

import com.orion.Usuarios.DTO.RegisterRequest;
import com.orion.Usuarios.DTO.RegisterResponse;
import com.orion.Usuarios.DTO.UsuarioResponseDTO;
import com.orion.Usuarios.DTO.UsuarioUpdateDTO;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock private UsuarioService usuarioService;
    @InjectMocks private UsuarioController controller;

    @Test
    @DisplayName("registrar: retorna 200 con los datos del usuario creado")
    void registrar_ok() {
        RegisterRequest request = mock(RegisterRequest.class);
        when(request.getUsername()).thenReturn("ana");
        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(1L);
        when(usuario.getUsername()).thenReturn("ana");
        when(usuario.getEmail()).thenReturn("ana@orion.cl");
        when(usuarioService.registrarUsuario(request)).thenReturn(usuario);

        ResponseEntity<RegisterResponse> resp = controller.registrar(request);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("ana", resp.getBody().getUsername());
    }

    @Test
    @DisplayName("obtenerNombrePorId: retorna el username del usuario")
    void obtenerNombre_ok() {
        UsuarioResponseDTO dto = mock(UsuarioResponseDTO.class);
        when(dto.getUsername()).thenReturn("ana");
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(dto);

        ResponseEntity<String> resp = controller.obtenerNombrePorId(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("ana", resp.getBody());
    }

    @Test
    @DisplayName("buscar: retorna el usuario por id")
    void buscar_ok() {
        UsuarioResponseDTO dto = mock(UsuarioResponseDTO.class);
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(dto);

        ResponseEntity<UsuarioResponseDTO> resp = controller.buscar(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(dto, resp.getBody());
    }

    @Test
    @DisplayName("buscarTodosUsuarios: retorna la lista completa")
    void buscarTodos_ok() {
        when(usuarioService.obtenerTodosUsuarios())
                .thenReturn(List.of(mock(UsuarioResponseDTO.class), mock(UsuarioResponseDTO.class)));

        ResponseEntity<List<UsuarioResponseDTO>> resp = controller.buscarTodosUsuarios();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2, resp.getBody().size());
    }

    @Test
    @DisplayName("actualizarUsuario: delega en el servicio y retorna 200")
    void actualizar_ok() {
        UsuarioResponseDTO dto = mock(UsuarioResponseDTO.class);
        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioUpdateDTO.class))).thenReturn(dto);

        ResponseEntity<UsuarioResponseDTO> resp =
                controller.actualizarUsuario(1L, mock(UsuarioUpdateDTO.class), 1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(dto, resp.getBody());
    }

    @Test
    @DisplayName("eliminarUsuario: delega en el servicio y retorna 200")
    void eliminar_ok() {
        ResponseEntity<String> resp = controller.eliminarUsuario(1L, 1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(usuarioService).eliminarUsuario(1L);
    }
}
