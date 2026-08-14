package com.treeaxes.Orion.Controller;

import com.treeaxes.Orion.DTO.UsuarioRequest;
import com.treeaxes.Orion.DTO.UsuarioResponse;
import com.treeaxes.Orion.Model.Usuario;
import com.treeaxes.Orion.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private ResponseEntity<UsuarioResponse> agregarUsuario(UsuarioRequest u){
        UsuarioResponse response = usuarioService.agregarUsuario(u);
        return ResponseEntity.ok(response);
    }
}
