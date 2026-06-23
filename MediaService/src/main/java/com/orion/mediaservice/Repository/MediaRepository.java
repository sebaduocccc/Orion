package com.orion.mediaservice.Repository;


import com.orion.mediaservice.Entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByNombreGenerado(String nombreGenerado);
}
