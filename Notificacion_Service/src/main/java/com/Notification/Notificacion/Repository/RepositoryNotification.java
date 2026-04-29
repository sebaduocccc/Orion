package com.Notification.Notificacion.Repository;

import com.Notification.Notificacion.Entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositoryNotification extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUserid(Long userid);

}
