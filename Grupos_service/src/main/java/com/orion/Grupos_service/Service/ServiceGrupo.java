package com.orion.Grupos_service.Service;

import com.orion.Grupos_service.Dto.MapperGrupo;
import com.orion.Grupos_service.Dto.RequestGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import com.orion.Grupos_service.Entity.Grupo;
import com.orion.Grupos_service.Repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceGrupo {
    @Autowired
    private Repository repo;
    @Autowired
    private MapperGrupo mapper;
    @Transactional
    public ResponseGrupo guardar(RequestGrupo dto, Long idCreador) {
        Grupo grupo = mapper.toEntity(dto,idCreador);
        Grupo guardado = repo.save(grupo);
        return mapper.toResponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<ResponseGrupo> obtenerTodos() {

        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseGrupo actualizar(Long id, RequestGrupo dto, Long idUsuario) {

        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        if (!grupo.getIdCreador().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para editar este grupo");
        }

        grupo.setNombre(dto.getNombre());
        grupo.setDescripcion(dto.getDescripcion());


        Grupo actualizado = repo.save(grupo);
        return mapper.toResponse(actualizado);
    }
    @Transactional
    public void eliminar(Long id, Long idUsuario) {

        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));

        if (!grupo.getIdCreador().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para eliminar este grupo");
        }

        repo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public ResponseGrupo obtenerPorId(Long id) {
        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        return mapper.toResponse(grupo);
    }
    public List<Long> obtenerMiembros(Long idGrupo) {
        Grupo grupo = repo.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        return grupo.getMiembros();
    }
    @Transactional
    public ResponseGrupo unirseAGrupo(Long id, Long idUsuario) {
        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));

        if (grupo.getMiembros().contains(idUsuario)) {
            throw new RuntimeException("Ya estás registrado en este grupo");
        }

        grupo.getMiembros().add(idUsuario);

        Grupo actualizado = repo.save(grupo);
        return mapper.toResponse(actualizado);
    }
}
