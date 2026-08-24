package com.treeaxes.Orion.Repository;

import com.treeaxes.Orion.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByName(String name);

    // Boolean detector de usuario existentes.
    boolean existsByName(String name);

}
