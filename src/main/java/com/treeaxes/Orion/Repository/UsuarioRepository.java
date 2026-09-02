package com.treeaxes.Orion.Repository;

import com.treeaxes.Orion.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    // Encontrar Usuario
    Optional<Usuario> findByUsername(String username);

    // Devolver "true" si el Usuario existe
    boolean existsByUsername(String username);

}
