package com.Notification.Notificacion.Dto;

import com.Notification.Notificacion.Entity.Notificacion;

public class Notificacion_mapper {
    public Notificacion aEntidad(Notificacion_Request request) {
        if (request == null) {
            return null;
        }
        Notificacion noti = new Notificacion();
        noti.setUserId(request.getUserId());
        noti.setSenderId(request.getSenderId());
        noti.setContent(request.getContent());
        noti.setTipoNotificacion(request.getTipoNotificacion());
        noti.setId_post(request.getIdpost());
        return noti;
    }
    public Notificacion_Response response(Notificacion notificacion){
        if(notificacion == null){
            return null;
        }
        return new Notificacion_Response(notificacion.getId(), notificacion.getUserId(), notificacion.getContent(), notificacion.getTipoNotificacion(), notificacion.isRead(), notificacion.getCreadoEl());


    }
}
