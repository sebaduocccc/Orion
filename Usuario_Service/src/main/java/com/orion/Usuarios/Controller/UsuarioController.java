package com.orion.Usuarios.Controller;


import com.orion.Usuarios.DTO.*;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@SecurityRequirement(name = "bearerAuth") // para el api-gateway y su jwt
//@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    // CRUD

    // CREATE
    @PostMapping("/registro")
    public ResponseEntity<RegisterResponse> registrar(@RequestBody RegisterRequest registerRequest) {
        log.info("POST /api/usuarios/registro - Registrando nuevo usuario: {}", registerRequest.getUsername());
        Usuario userRegistrado = usuarioService.registrarUsuario(registerRequest);
        log.info("Usuario registrado con id={}", userRegistrado.getId());
        return ResponseEntity.ok(new RegisterResponse(
                userRegistrado.getId(),
                userRegistrado.getUsername(),
                userRegistrado.getEmail()
        ));
    }


    // READ
    @GetMapping("/nombre/{id}")
    public ResponseEntity<String> obtenerNombrePorId(@PathVariable Long id) {
        log.info("GET /api/usuarios/nombre/{}", id);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id).getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        log.info("GET /api/usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorUsername(@PathVariable String username) {
        log.info("GET /api/usuarios/username/{}", username);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorUsername(username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodosUsuarios() {
        log.info("GET /api/usuarios/all - Listando todos los usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodosUsuarios());
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO updateDTO,
            @AuthenticationPrincipal Long userId) {
        log.info("PUT /api/usuarios/{} - Solicitado por userId={}", id, userId);
        UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(id, updateDTO);
        log.info("Usuario id={} actualizado correctamente", id);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        log.info("DELETE /api/usuarios/{} - Solicitado por userId={}", id, userId);
        usuarioService.eliminarUsuario(id);
        log.info("Usuario id={} eliminado correctamente", id);
        return ResponseEntity.ok("Usuario eliminado");
    }


    // FRONTEND APP

    @GetMapping("/profile/photo/{userId}")
    public ResponseEntity<ProfilePhotoDTO> buscarAvatarUrl(@PathVariable Long userId) {
        log.info("GET /api/usuarios/profile/photo/{} - Obteniendo avatar", userId);
        UsuarioPerfil usuarioPerfil = usuarioService.obtenerUsuarioPerfilPorId(userId);
        return ResponseEntity.ok(new ProfilePhotoDTO(usuarioPerfil.getAvatarUrl()));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUsuarioPerfil(@PathVariable Long userId) {
        log.info("GET /api/usuarios/profile/{} - Obteniendo perfil público", userId);
        UsuarioResponseDTO user = usuarioService.obtenerUsuarioPorId(userId);
        UserProfileResponse perfil = new UserProfileResponse(
                user.getUsername(),
                user.getAvatarUrl(),
                user.getBiografia(),
                user.getUbicacion()
        );
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<UsuarioPerfil> actualizarUrlAvatar(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long userId) {
        log.info("PUT /api/usuarios/{}/avatar - Solicitado por userId={}", id, userId);
        String nuevaUrl = body.get("avatarUrl");
        UsuarioPerfil perfilActualizado = usuarioService.actualizarUrlAvatar(id, nuevaUrl);
        log.info("Avatar actualizado para usuario id={}", id);
        return ResponseEntity.ok(perfilActualizado);
    }


}
