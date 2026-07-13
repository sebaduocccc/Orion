package com.orion.Grupos_service.Dto;

import com.orion.Grupos_service.Client.UsuarioClient;
import com.orion.Grupos_service.Entity.Grupo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MapperGrupo {
    @Autowired
    private  UsuarioClient usuarioClient;

    // Si Usuario_Service no responde o el usuario ya no existe, no se cae todo el listado
    private String resolverNombre(Long id) {
        try {
            return usuarioClient.obtenerNombrePorId(id);
        } catch (Exception ex) {
            log.warn("No se pudo resolver el nombre del usuario={}: {}", id, ex.getMessage());
            return "usuario_" + id;
        }
    }

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
                        .map(this::resolverNombre)
                        .collect(Collectors.toList())
        );
        response.setCreadoEl(e.getCreadoEl());

        return response;









    }
}
