package com.Interacciones.Interacciones.Repository;

import com.Interacciones.Interacciones.Entity.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Repository_Interaccion extends JpaRepository<Interaccion, Long> {
    List<Interaccion> findByUserid(Long userid);


    List<Interaccion> findByPostid(Long postid);


    List<Interaccion> findByTipo(String tipo);
    long countByPostidAndTipo(Long postid, String tipo);
}
