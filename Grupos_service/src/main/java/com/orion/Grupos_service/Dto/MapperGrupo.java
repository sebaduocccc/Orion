package com.orion.Grupos_service.Dto;

import com.orion.Grupos_service.Client.UsuarioClient;
import com.orion.Grupos_service.Entity.Grupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperGrupo {
    @Autowired
    private  UsuarioClient usuarioClient;

    public Grupo toEntity(RequestGrupo dto, Long idCreador) {
        if (dto == null) return null;

        Grupo grupo = new Grupo();
        grupo.setNombre(dto.getNombre());
        grupo.setDescripcion(dto.getDescripcion());
        grupo.setIdCreador(idCreador);


        grupo.setMiembros(new ArrayList<>());
        grupo.getMiembros().add(idCreador);
        grupo.setCreadoEl(LocalDateTime.now());

        return grupo;
    }

    public ResponseGrupo toResponse(Grupo e) {


        ResponseGrupo response = new ResponseGrupo();
        response.setIdGrupo(e.getIdGrupo());
        response.setIdCreador(e.getIdCreador());
        response.setNombre(e.getNombre());
        response.setDescripcion(e.getDescripcion());


        response.setMiembros(
                e.getMiembros().stream()
                        .map(id -> usuarioClient.obtenerNombrePorId(id))
                        .collect(Collectors.toList())
        );

        return response;









    }
}
