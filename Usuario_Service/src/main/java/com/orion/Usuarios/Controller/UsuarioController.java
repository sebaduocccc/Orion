package com.orion.Usuarios.Controller;


import com.orion.Usuarios.Model.Usuario;
import com.orion.Usuarios.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario){
        Usuario nuevoUser = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUser);
    }


    @GetMapping("/{username}")
    public ResponseEntity<Usuario> obtenerPorUsername(@PathVariable String username){
        return usuarioService.obtenerPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
