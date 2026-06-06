package com.orion.Grupos_service.Dto;

import com.orion.Grupos_service.Entity.Grupo;

import java.util.ArrayList;

public class MapperGrupo {
    public Grupo toEntity(RequestGrupo dto, Long idCreador) {
        if (dto == null) return null;

        Grupo grupo = new Grupo();
        grupo.setNombre(dto.getNombre());
        grupo.setDescripcion(dto.getDescripcion());
        grupo.setIdCreador(idCreador);


        grupo.setMiembros(new ArrayList<>());
        grupo.getMiembros().add(idCreador);

        return grupo;
    }

    public ResponseGrupo toResponse(Grupo entity) {


        ResponseGrupo response = new ResponseGrupo();
        response.setIdGrupo(entity.getIdGrupo());
        response.setIdCreador(entity.getIdCreador());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setMiembros(entity.getMiembros());
        response.setCreadoEl(entity.getCreadoEl());

        return response;
    }
}
