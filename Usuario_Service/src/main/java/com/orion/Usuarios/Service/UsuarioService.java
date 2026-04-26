package com.orion.Usuarios.Service;


import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Repository.RolRepository;
import com.orion.Usuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));


        Rol userRole = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol no existe"));


        Set<Rol> roles = new HashSet<>();

        roles.add(userRole);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }


    public Optional<Usuario> obtenerPorUsername(String username){
        return usuarioRepository.findByUsername(username);
    }

}
