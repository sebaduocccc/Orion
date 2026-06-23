package com.orion.interaccion.Service;


import com.orion.interaccion.Entity.Like;
import com.orion.interaccion.Repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class InteraccionService {

    @Autowired
    private LikeRepository repo;


    public boolean toggleLike(Long usuarioId, Long postId) {
        Optional<Like> likeExistente = repo.findByUsuarioIdAndPostId(usuarioId, postId);

        if (likeExistente.isPresent()) {
            repo.delete(likeExistente.get());
            log.warn("Like eliminado: usuario={} ya había dado like al post={}", usuarioId, postId);
            return false;
        } else {
            Like like = new Like();
            like.setUsuarioId(usuarioId);
            like.setPostId(postId);
            repo.save(like);
            log.info("Like registrado exitosamente: usuario={} post={}", usuarioId, postId);
            return true;
        }
    }


    public long obtenerTotalLikes(Long postId) {
        long total = repo.countByPostId(postId);
        log.info("Total de likes consultado: post={} total={}", postId, total);
        return total;
    }


    public boolean verificarSiLikeo(Long userId, Long postId) {
        boolean liked = repo.findByUsuarioIdAndPostId(userId, postId).isPresent();
        log.info("Verificación de like: usuario={} post={} liked={}", userId, postId, liked);
        return liked;
    }


}
