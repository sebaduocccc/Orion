package com.orion.eventos_service.DTO;

import com.orion.eventos_service.Client.UsuarioClient;
import com.orion.eventos_service.Entity.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventoMapper {
    @Autowired
    private UsuarioClient usuarioClient;
    public EventoResponse aResponse(Evento e ){
        EventoResponse dto = new EventoResponse();
        dto.setIdEvento(e.getIdEvento());
        dto.setIdCreador(e.getIdCreador());

        dto.setFecha(e.getFecha());
        dto.setCreadoEl(e.getCreadoEl());
        dto.setLugar(e.getLugar());
        dto.setNombre(e.getNombre());
        List<String> nombresAsistentes = (e.getAsistentes() != null)
                ? e.getAsistentes().stream().map(id -> usuarioClient.obtenerNombrePorId(id)).collect(Collectors.toList())
                : new ArrayList<>();

        dto.setAsistentes(nombresAsistentes);


        if (e.getAsistentes() != null) {
            dto.setCantidadAsistentes(e.getAsistentes().size());
        } else {
            dto.setCantidadAsistentes(0);
        }
        return dto;

    }
    public Evento aEntidad(EventoRequest er, Long creadorId){
        Evento e = new Evento();
        e.setIdCreador(creadorId);
        e.setNombre(er.getNombre());
        e.setLugar(er.getLugar());
        e.setFecha(er.getFecha());

        return e;

    }
}
