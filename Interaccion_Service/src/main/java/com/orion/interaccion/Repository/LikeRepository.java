package com.orion.interaccion.Repository;

import com.orion.interaccion.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // contar los likes de un post
    long countByPostId(Long postId);

    // verifica si un usuario ya dio like a un post para asi marcarlo en el frontend como likeado
    boolean existsByUsuarioIdAndPostId(Long usuarioId, Long postId);

    // Busca un like especifico para borrarlo (quitar like)
    Optional<Like> findByUsuarioIdAndPostId(Long usuarioId, Long postId);

}
