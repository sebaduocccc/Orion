package com.treeaxes.Orion.Service;

import com.treeaxes.Orion.DTO.UsuarioRequest;
import com.treeaxes.Orion.DTO.UsuarioResponse;
import com.treeaxes.Orion.Model.Usuario;
import com.treeaxes.Orion.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para agregar un nuevo usuario
    public UsuarioResponse agregarUsuario(UsuarioRequest u){
        Usuario usuario = new Usuario();
        usuario.setUsername(u.getUsername());
        usuario.setPassword(u.getPassword());
        usuarioRepository.save(usuario);

        UsuarioResponse res = new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getCreatedAt());
        return res;
    }

    // Metodo para obtener un usuario por su ID
    public UsuarioResponse obtenerUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if(usuario == null) return null; // Si el usuario es Null, retornamos null para indicar que no se encontró

        UsuarioResponse res = new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getCreatedAt());
        return res;
    }

    // Metodo para obtener todos los usuarios
    public List<UsuarioResponse> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getCreatedAt()
                ))
                .toList();
    }

    // Metodo para eliminar un usuario por su ID
    public UsuarioResponse eliminarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if(usuario == null) return null;

        usuarioRepository.delete(usuario);
        UsuarioResponse res = new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getCreatedAt());
        return res;
    }
}
