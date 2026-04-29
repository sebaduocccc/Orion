package com.Notification.Notificacion.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones", indexes = {
        @Index(name = "idx_notificacion_user", columnList = "userId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El userId es requerido")
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "El senderId es requerido")
    @Column(nullable = false)
    private Long senderId;

    @NotBlank(message = "EL CONTENIDO ES REQUERIDO")
    @Column(nullable = false)
    private String content;

    @NotBlank(message = "EL TIPO DE NOTIFICACIÓN ES REQUERIDO")
    @Column(nullable = false)
    private String tipoNotificacion;


    private Long id_post;

    @Column(nullable = false)
    private boolean isRead = false;


    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;
}