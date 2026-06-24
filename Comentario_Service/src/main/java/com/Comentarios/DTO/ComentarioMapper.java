package com.Comentarios.DTO;

import com.Comentarios.Entity.Comentario;
import org.springframework.stereotype.Component;

@Component
public class ComentarioMapper {

    public Comentario aEntidad(RequestComentario dto, Long postId, Long userId) {
        Comentario comentario = new Comentario();
        comentario.setPostId(postId);
        comentario.setUserId(userId);
        comentario.setContenido(dto.getContenido());
        return comentario;
    }

    public ResponseComentario response(Comentario comentario) {
        ResponseComentario dto = new ResponseComentario();
        dto.setId(comentario.getId());
        dto.setPostId(comentario.getPostId());
        dto.setUserId(comentario.getUserId());
        dto.setContenido(comentario.getContenido());
        dto.setCreadoEl(comentario.getCreadoEl());
        return dto;
    }
}
