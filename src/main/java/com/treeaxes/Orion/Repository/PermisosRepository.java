package com.treeaxes.Orion.Repository;

import com.treeaxes.Orion.Model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisosRepository extends JpaRepository<Permiso,Long> {

    // Encontrar permiso por su nombre
    Optional<Permiso> findByName(String name);
}
