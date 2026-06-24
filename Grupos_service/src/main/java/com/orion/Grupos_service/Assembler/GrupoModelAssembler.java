package com.orion.Grupos_service.Assembler;

import com.orion.Grupos_service.Controller.ControllerGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GrupoModelAssembler implements RepresentationModelAssembler<ResponseGrupo, EntityModel<ResponseGrupo>> {
    @Override
    public EntityModel<ResponseGrupo> toModel(ResponseGrupo grupo) {
        return EntityModel.of(grupo,
                linkTo(methodOn(ControllerGrupo.class)
                        .verGrupos(grupo.getIdGrupo())).withRel("Viendo todos los grupos"),
                linkTo(methodOn(ControllerGrupo.class)
                        .actualizar(grupo.getIdGrupo(), null)).withRel("Actualizando grupo "),
                linkTo(methodOn(ControllerGrupo.class)
                        .borrar(grupo.getIdGrupo())).withRel("Eliminar Grupo "),
                linkTo(methodOn(ControllerGrupo.class).unirse(grupo.getIdGrupo())).withRel("Unirse a grupo")
        );
    }
}
