package com.posteos.Service;


import com.posteos.Entity.Post;
import com.posteos.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ServicePost {

    @Autowired
    private PostRepository repository;

    public Post guardarPost(Post post) {
        post.setCreadoEl(new Date(System.currentTimeMillis()));
        return repository.save(post);
    }


    public List<Post> listarPorPostsPorUsuario(Long userId){
        return repository.findByUseridOrderByCreadoElDesc(userId);
    }

}
