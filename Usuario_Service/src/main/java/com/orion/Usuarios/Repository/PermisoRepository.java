package com.orion.Usuarios.Repository;

import com.orion.Usuarios.Entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

    Optional<Permiso> findByNombre(String nombre);
}
