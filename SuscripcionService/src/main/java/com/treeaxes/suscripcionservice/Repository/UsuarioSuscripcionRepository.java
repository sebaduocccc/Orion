package com.treeaxes.suscripcionservice.Repository;

import com.treeaxes.suscripcionservice.Modelo.UsuarioSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioSuscripcionRepository extends JpaRepository<UsuarioSuscripcion,Long> {
    List<UsuarioSuscripcion> findByUserId(Long userId);
}
