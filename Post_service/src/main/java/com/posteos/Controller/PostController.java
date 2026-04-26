package com.posteos.Controller;


import com.posteos.DTO.PostRequest;
import com.posteos.Entity.Post;
import com.posteos.Repository.PostRepository;
import com.posteos.Service.ServicePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private ServicePost servicePost;

    @PostMapping("/crear")
    public ResponseEntity<Post> crear(@RequestBody PostRequest request){

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        Post nuevoPost = new Post();
        nuevoPost.setUserid(userId);
        nuevoPost.setContent(request.getContent());
        nuevoPost.setMediaurl(request.getMediaUrl());

        return ResponseEntity.ok(servicePost.guardarPost(nuevoPost));
    }


    @PostMapping("/mis-posts")
    public ResponseEntity<List<Post>> obtenerMisPosts{

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return ResponseEntity.ok(servicePost.listarPorPostsPorUsuario(userId));

    }



}
