package com.orion.interaccion.Service;


import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Repository.FollowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class FollowService {

    @Autowired
    private FollowRepository repo;


    public boolean toggleFollow(Long seguidorId, Long seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            log.warn("Intento de auto-seguimiento rechazado: usuario={} intentó seguirse a sí mismo", seguidorId);
            throw new RuntimeException("no puedes seguirte a ti mismo.");
        }

        Optional<Follow> followExistente = repo.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId);

        if (followExistente.isPresent()) {
            repo.delete(followExistente.get());
            log.info("Unfollow registrado: seguidor={} dejó de seguir a seguido={}", seguidorId, seguidoId);
            return false;
        } else {
            Follow newFollow = new Follow();
            newFollow.setSeguidorId(seguidorId);
            newFollow.setSeguidoId(seguidoId);
            repo.save(newFollow);
            log.info("Follow registrado exitosamente: seguidor={} ahora sigue a seguido={}", seguidorId, seguidoId);
            return true;
        }
    }


    public long obtenerContadorSeguidores(Long userId) {
        long count = repo.countBySeguidoId(userId);
        log.info("Conteo de seguidores: usuario={} seguidores={}", userId, count);
        return count;
    }

    public long obtenerContadorSeguidos(Long userId) {
        long count = repo.countBySeguidorId(userId);
        log.info("Conteo de seguidos: usuario={} seguidos={}", userId, count);
        return count;
    }

    public boolean verificarSiSigue(Long seguidorId, Long seguidoId) {
        boolean sigue = repo.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId).isPresent();
        log.info("Verificación de follow: seguidor={} seguido={} sigue={}", seguidorId, seguidoId, sigue);
        return sigue;
    }

}
