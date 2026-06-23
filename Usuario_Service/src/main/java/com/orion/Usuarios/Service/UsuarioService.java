package com.orion.Usuarios.Service;


import com.orion.Usuarios.DTO.RegisterRequest;
import com.orion.Usuarios.DTO.UsuarioResponseDTO;
import com.orion.Usuarios.DTO.UsuarioUpdateDTO;
import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Exception.ResourceAlreadyExistsException;
import com.orion.Usuarios.Exception.ResourceNotFoundException;
import com.orion.Usuarios.Repository.RolRepository;
import com.orion.Usuarios.Repository.UserProfileRepository;
import com.orion.Usuarios.Repository.UsuarioRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    WebClient.Builder webClientBuilder;


    // CREATE
    public Usuario registrarUsuario(RegisterRequest registerRequest) {
        log.info("Intentando registrar usuario: {}", registerRequest.getUsername());

        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registro rechazado: el username '{}' ya está en uso", registerRequest.getUsername());
            throw new ResourceAlreadyExistsException("Usuario con nombre " + registerRequest.getUsername() + " ya esta registrado.");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registro rechazado: el email ya está registrado para usuario '{}'", registerRequest.getUsername());
            throw new ResourceAlreadyExistsException("El correo electronico ya esta registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setBiografia(registerRequest.getBiografia());
        usuarioPerfil.setAvatarUrl("/api/media/avatar/default_avatar.png");
        usuarioPerfil.setUbicacion(registerRequest.getUbicacion());
        usuario.setPerfil(usuarioPerfil);

        Rol userRole = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol no existe"));
        Set<Rol> roles = new HashSet<>();
        roles.add(userRole);
        usuario.setRoles(roles);

        usuarioPerfil.setUsuario(usuario);
        usuario.setPerfil(usuarioPerfil);

        Usuario userGuardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con id={}", userGuardado.getId());
        return userGuardado;
    }


    // READ
    public List<UsuarioResponseDTO> obtenerTodosUsuarios() {
        log.info("Obteniendo lista completa de usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            log.warn("No hay usuarios registrados en el sistema");
            throw new ResourceNotFoundException("No hay Usuarios registrados");
        }

        List<UsuarioResponseDTO> usuarioResponseDTOs = new ArrayList<>();
        for (Usuario u : usuarios) {
            usuarioResponseDTOs.add(new UsuarioResponseDTO(
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getPerfil().getAvatarUrl(),
                    u.getPerfil().getBiografia(),
                    u.getPerfil().getUbicacion(),
                    conseguirPostCount(u.getId()),
                    conseguirSeguidoresUsuario(u.getId()),
                    conseguirSeguidosUsuario(u.getId())
            ));
        }

        log.info("Se retornaron {} usuarios", usuarioResponseDTOs.size());
        return usuarioResponseDTOs;
    }

    public long conseguirPostCount(Long userId) {
        Long count = webClientBuilder.build()
                .get()
                .uri("lb://Posteos/api/posts/user/" + userId + "/count")
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorReturn(0L)
                .block();
        long result = count != null ? count : 0L;
        log.info("Post count para usuario={}: {}", userId, result);
        return result;
    }

    public long conseguirSeguidoresUsuario(Long userId) {
        Long count = webClientBuilder.build()
                .get()
                .uri("lb://Interaccion_Service/api/interacciones/usuarios/" + userId + "/seguidores/count")
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorReturn(0L)
                .block();
        long result = count != null ? count : 0L;
        log.info("Seguidores de usuario={}: {}", userId, result);
        return result;
    }

    public long conseguirSeguidosUsuario(Long userId) {
        Long count = webClientBuilder.build()
                .get()
                .uri("lb://Interaccion_Service/api/interacciones/usuarios/" + userId + "/seguidos/count")
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorReturn(0L)
                .block();
        long result = count != null ? count : 0L;
        log.info("Seguidos de usuario={}: {}", userId, result);
        return result;
    }

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario con id={}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPerfil().getAvatarUrl(),
                usuario.getPerfil().getBiografia(),
                usuario.getPerfil().getUbicacion(),
                conseguirPostCount(usuario.getId()),
                conseguirSeguidoresUsuario(usuario.getId()),
                conseguirSeguidosUsuario(usuario.getId())
        );
    }

    public UsuarioResponseDTO obtenerUsuarioPorUsername(String username) {
        log.info("Buscando usuario con username={}", username);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPerfil().getAvatarUrl(),
                usuario.getPerfil().getBiografia(),
                usuario.getPerfil().getUbicacion(),
                conseguirPostCount(usuario.getId()),
                conseguirSeguidoresUsuario(usuario.getId()),
                conseguirSeguidosUsuario(usuario.getId())
        );
    }

    public UsuarioPerfil obtenerUsuarioPerfilPorId(Long id) {
        log.info("Buscando perfil del usuario id={}", id);
        return userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));
    }


    // UPDATE
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Actualizando usuario id={}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id"));

        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado para usuario id=" + id));

        if (usuarioUpdateDTO.getUsername() != null && !usuarioUpdateDTO.getUsername().isEmpty()) {
            usuario.setUsername(usuarioUpdateDTO.getUsername());
        }
        if (usuarioUpdateDTO.getEmail() != null && !usuarioUpdateDTO.getEmail().isEmpty()) {
            usuario.setEmail(usuarioUpdateDTO.getEmail());
        }
        if (usuarioUpdateDTO.getPassword() != null && !usuarioUpdateDTO.getPassword().isEmpty()) {
            usuario.setPassword(usuarioUpdateDTO.getPassword());
        }
        if (usuarioUpdateDTO.getAvatarUrl() != null && !usuarioUpdateDTO.getAvatarUrl().isEmpty()) {
            usuarioPerfil.setAvatarUrl(usuarioUpdateDTO.getAvatarUrl());
        }
        if (usuarioUpdateDTO.getBiografia() != null && !usuarioUpdateDTO.getBiografia().isEmpty()) {
            usuarioPerfil.setBiografia(usuarioUpdateDTO.getBiografia());
        }
        if (usuarioUpdateDTO.getUbicacion() != null && !usuarioUpdateDTO.getUbicacion().isEmpty()) {
            usuarioPerfil.setUbicacion(usuarioUpdateDTO.getUbicacion());
        }

        usuarioRepository.save(usuario);
        userProfileRepository.save(usuarioPerfil);
        log.info("Usuario id={} actualizado exitosamente", id);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPerfil().getAvatarUrl(),
                usuario.getPerfil().getBiografia(),
                usuario.getPerfil().getUbicacion(),
                conseguirPostCount(usuario.getId()),
                conseguirSeguidoresUsuario(usuario.getId()),
                conseguirSeguidosUsuario(usuario.getId())
        );
    }

    // DELETE
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario id={}", id);
        if (!usuarioRepository.existsById(id)) {
            log.warn("Intento de eliminar usuario inexistente id={}", id);
            throw new ResourceNotFoundException("Usuario no encontrado con el id " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario id={} eliminado exitosamente", id);
    }


    public UsuarioPerfil actualizarUrlAvatar(Long id, String nuevaUrl) {
        log.info("Actualizando avatar del usuario id={}", id);
        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));
        usuarioPerfil.setAvatarUrl(nuevaUrl);
        UsuarioPerfil guardado = userProfileRepository.save(usuarioPerfil);
        log.info("Avatar actualizado para usuario id={}", id);
        return guardado;
    }
}
