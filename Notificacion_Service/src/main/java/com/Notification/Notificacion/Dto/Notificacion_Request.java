package com.Notification.Notificacion.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion_Request {
    @NotNull
    private Long userId;
    @NotNull
    private Long senderId;
    @NotBlank
    private String content;
    @NotBlank
    private String tipoNotificacion;

    private Long idpost;


}
