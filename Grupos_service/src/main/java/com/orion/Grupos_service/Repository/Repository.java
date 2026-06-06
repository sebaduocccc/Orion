package com.orion.Grupos_service.Repository;

import com.orion.Grupos_service.Entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Grupo, Long> {
}
