package com.orion.interaccion.Repository;

import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // cuantos seguidores tiene el usuario X
    long countBySeguidoId(Long seguidoId);

    // A cuantas personas sigue el  usuario X
    long countBySeguidorId(Long seguidorId);

    // Para dejar de seguir
    Optional<Follow> findBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);

}
