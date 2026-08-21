package com.treeaxes.Orion.Repository;

import com.treeaxes.Orion.Model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje,Long> {
}
