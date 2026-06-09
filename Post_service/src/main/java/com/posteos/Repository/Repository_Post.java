package com.posteos.Repository;

import com.posteos.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repository_Post extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);


    long countByUserId(Long userId);

    List<Post> findByUserIdOrderByCreadoElDesc(Long userId);


    // Para el feed de forma descendente
    Page<Post> findAllByOrderByCreadoElDesc(Pageable pageable);

    List<Post> findByIdGrupo(Long idGrupo);

    // para el feed de publicaciones de un usuario concreto
    Page<Post> findByUserIdOrderByCreadoElDesc(Long userId, Pageable pageable);

}
