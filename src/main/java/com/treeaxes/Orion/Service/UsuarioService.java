package com.treeaxes.Orion.Service;

import com.treeaxes.Orion.DTO.UsuarioRequest;
import com.treeaxes.Orion.DTO.UsuarioResponse;
import com.treeaxes.Orion.Model.Usuario;
import com.treeaxes.Orion.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioResponse agregarUsuario(UsuarioRequest u){
        Usuario usuario = new Usuario();
        usuario.setUsername(u.getUsername());
        usuario.setPassword(u.getPassword());
        usuarioRepository.save(usuario);

        UsuarioResponse res = new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getCreatedAt());
        return res;
    }

    public UsuarioResponse obtenerUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if(usuario == null){
            return null;
        }

        UsuarioResponse res = new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getCreatedAt());
        return res;
    }
}
