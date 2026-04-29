package com.Notification.Notificacion.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion_Response {
    private Long id;
    private Long userId;
    private String content;
    private String tipoNotificacion;
    private boolean read;
    private LocalDateTime creadoEl;

}
