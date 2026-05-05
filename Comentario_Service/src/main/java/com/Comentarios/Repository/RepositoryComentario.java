package com.Comentarios.Repository;

import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RepositoryComentario extends JpaRepository<Comentario, Long> {
    public long countByPostId(Long postId);

    public List<ResponseComentario> findByPostId(Long postId);
    public List<ResponseComentario> findByUserId(Long userId);
}

