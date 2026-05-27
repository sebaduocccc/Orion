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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class UsuarioService {

    // inyecciones
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



    // CRUD

    // CREATE
    public Usuario registrarUsuario(RegisterRequest registerRequest) {

        // verificacion antes
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Usuario con nombre "+ registerRequest.getUsername()+ " ya esta registrado.");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo electronico ya esta registrado.");
        }

        // se crea usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // se crea el perfil de usuario
        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setBiografia(registerRequest.getBiografia());
        usuarioPerfil.setAvatarUrl("/api/media/avatar/default_avatar.png");
        usuarioPerfil.setUbicacion(registerRequest.getUbicacion());
        usuario.setPerfil(usuarioPerfil);

        // se asigna rol predeterminado
        Rol userRole = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol no existe"));
        Set<Rol> roles = new HashSet<>();
        roles.add(userRole);
        usuario.setRoles(roles);


        usuarioPerfil.setUsuario(usuario);
        usuario.setPerfil(usuarioPerfil);

        // se guarda
        Usuario userGuardado = usuarioRepository.save(usuario);
        return userGuardado;
    }


    // READ
    public List<UsuarioResponseDTO> obtenerTodosUsuarios() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
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

        return usuarioResponseDTOs;
    }

    public long conseguirPostCount(Long userId){
        String token = obtenerTokenActual();
        Long cantidadPosts = webClientBuilder.build()
                .get()
                .uri("http://api-gateway:8000/api/posts/user/"+userId+"/count")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
        return cantidadPosts != null ? cantidadPosts : 0L;
    }

    public long conseguirSeguidoresUsuario(Long userId){
        Long cantidadSeguidores = webClientBuilder.build()
                .get()
                .uri("http://api-gateway:8000/api/interacciones/usuarios/"+userId+"/seguidores/count")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
        return cantidadSeguidores != null ? cantidadSeguidores : 0L;
    }

    public long conseguirSeguidosUsuario(Long userId){
        Long cantidadSeguidos = webClientBuilder.build()
                .get()
                .uri("http://api-gateway:8000/api/interacciones/usuarios/"+ userId+"/seguidos/count")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
        return cantidadSeguidos != null ? cantidadSeguidos : 0L;
    }

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
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
        return usuarioResponseDTO;

    }

    public UsuarioResponseDTO obtenerUsuarioPorUsername(String username){

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
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
        return usuarioResponseDTO;
    }

    public UsuarioPerfil obtenerUsuarioPerfilPorId(Long id){
        return userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));
    }




    // UPDATE

    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id"));

        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id"));


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
    public void eliminarUsuario(Long id){

        if(!usuarioRepository.existsById(id)){
            throw new ResourceNotFoundException("Usuario no encontrado con el id "+ id);
        }

        ///  borrará el usuario y el userProfile juntos como cascada
        usuarioRepository.deleteById(id);
    }



    // jwt para llamar a otros servicios

    public String obtenerTokenActual() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader(HttpHeaders.AUTHORIZATION);
        }

        return null;
    }

//    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado){
//        Usuario usuarioExistente = obtenerUsuarioPorId(id);
//
//        if (usuarioActualizado.getUsername() != null &&
//        !usuarioActualizado.getUsername().equals(usuarioExistente.getUsername())) {
//
//            usuarioExistente.setUsername(usuarioActualizado.getUsername());
//        }
//
//        if (usuarioActualizado.getPassword() != null
//        && !usuarioActualizado.getPassword().isEmpty()){
//            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
//        }
//
//        return usuarioRepository.save(usuarioExistente);
//    }

    public UsuarioPerfil actualizarUrlAvatar(Long id, String nuevaUrl){
        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));

        usuarioPerfil.setAvatarUrl(nuevaUrl);

        return userProfileRepository.save(usuarioPerfil);
    }





}
