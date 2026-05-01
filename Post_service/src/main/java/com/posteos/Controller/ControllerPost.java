package com.posteos.Controller;

import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Service.ServicePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class ControllerPost {
    @Autowired
    private  ServicePost service;

    @PostMapping
    public ResponseEntity<PostResponseDTO> guardar (@RequestBody PostRequestDTO dto ){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        dto.setUserid(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(dto));


    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<PostResponseDTO> lista = service.obtenerTodos();
        if(lista.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO HAY POSTS");

        }
        return ResponseEntity.ok(lista);

    }
    @GetMapping("/{id}")
    public ResponseEntity<List<PostResponseDTO>> verPostsUser (@PathVariable Long id){
        return ResponseEntity.ok(service.buscarUser(id));


    }
    @DeleteMapping ("/{id}")
        public ResponseEntity<String> borrar(@PathVariable Long id){
            try{
                service.eliminar(id);
                return ResponseEntity.ok("Post eliminado correctamente");
            }catch (RuntimeException e ){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post no encontrado");

            }

        }

}
