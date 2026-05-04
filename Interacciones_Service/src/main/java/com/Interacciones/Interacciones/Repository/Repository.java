package com.Interacciones.Interacciones.Repository;

import com.Interacciones.Interacciones.Entity.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repository extends JpaRepository<Interaccion, Long> {
    List<Interaccion> findByUserid(Long userid);


    List<Interaccion> findByPostid(Long postid);


    List<Interaccion> findByTipo(String tipo);
}
