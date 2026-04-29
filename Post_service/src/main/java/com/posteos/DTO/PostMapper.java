package com.posteos.DTO;

import com.posteos.Entity.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {
    //convierte los datos de request a una entidad Post para guardar en la base de datos
    public Post aEntidad(PostRequestDTO pdto)
    {
        Post post = new Post(); //Se crea una entidad
        post.setUserid(pdto.getUserid());
        post.setContent(pdto.getContent());
        post.setMediaUrl(pdto.getMediaUrl());

        return post;


    }
    // De la base de datos  A afuera//
    public PostResponseDTO response(Post posteo){
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(posteo.getId());
        dto.setContent(posteo.getContent());
        dto.setUserid(posteo.getUserid());
        dto.setMediaUrl(posteo.getMediaUrl());
        dto.setCreado_el(posteo.getCreado_el());
        return dto;

    }
}
