package com.treeaxes.Orion.Repository;

import com.treeaxes.Orion.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

    // Encontrar rol por su nombre
    Optional<Rol> findByName(String name);

}
