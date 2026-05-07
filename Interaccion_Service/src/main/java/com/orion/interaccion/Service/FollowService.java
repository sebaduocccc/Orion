package com.orion.interaccion.Service;


import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository repo;

    // toggle de seguir a seguidoId
    public boolean toggleFollow(Long seguidorId, Long seguidoId){

        if(seguidorId.equals(seguidoId)){
            throw new RuntimeException("no puedes seguirte a ti mismo.");
        }

        Optional<Follow> followExistente = repo.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId);

        if(followExistente.isPresent()) {
            //si ya lo sigue entnces eliminamos el follow (unfollow)
            repo.delete(followExistente.get());
            return false;
        } else {
            // si no lo sigue se crea el follow
            Follow newFollow = new Follow();
            newFollow.setSeguidorId(seguidorId);
            newFollow.setSeguidoId(seguidoId);
            repo.save(newFollow);
            return true;
        }
    }




    public long obtenerContadorSeguidores(Long userId){
        return repo.countBySeguidoId(userId);
    }

    public long obtenerContadorSeguidos(Long userId){
        return repo.countBySeguidorId(userId);
    }

    public boolean verificarSiSigue(Long seguidorId, Long seguidoId){
        return repo.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId).isPresent();
    }

}
