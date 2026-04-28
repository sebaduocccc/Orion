package com.Notification.Notificacion.Repository;

import com.Notification.Notificacion.Entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RepositoryNotification extends JpaRepository<Notificacion, Long> {
}
