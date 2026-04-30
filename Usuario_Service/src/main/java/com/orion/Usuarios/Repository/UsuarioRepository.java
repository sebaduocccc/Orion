package com.orion.Usuarios.Repository;

import com.orion.Usuarios.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    // devolver true o false por si existe
    boolean existsByUsername(String username);

}
