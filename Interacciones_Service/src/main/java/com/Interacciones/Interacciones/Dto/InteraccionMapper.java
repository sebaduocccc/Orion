package com.Interacciones.Interacciones.Dto;

import com.Interacciones.Interacciones.Entity.Interaccion;
import org.springframework.stereotype.Component;

@Component
public class InteraccionMapper {

    public Interaccion aEntidad(InteraccionRequestDTO dto) {
        Interaccion interaccion = new Interaccion();
        interaccion.setUserid(dto.getUserid());
        interaccion.setPostid(dto.getPostid());
        interaccion.setTipo(dto.getTipo());
        return interaccion;
    }

    public InteraccionResponseDTO response(Interaccion interaccion) {
        InteraccionResponseDTO dto = new InteraccionResponseDTO();
        dto.setId(interaccion.getId());
        dto.setUserid(interaccion.getUserid());
        dto.setPostid(interaccion.getPostid());
        dto.setTipo(interaccion.getTipo());
        dto.setCreadoEl(interaccion.getCreadoEl());
        return dto;
    }
}