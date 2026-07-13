package com.orion.Grupos_service.Service;

import com.orion.Grupos_service.Dto.MapperGrupo;
import com.orion.Grupos_service.Dto.RequestGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import com.orion.Grupos_service.Entity.Grupo;
import com.orion.Grupos_service.Exceptions.ResourceNotFound;
import com.orion.Grupos_service.Repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Grupo creado: id={} nombre='{}' creador={}", guardado.getIdGrupo(), guardado.getNombre(), idCreador);
        return mapper.toResponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<ResponseGrupo> obtenerTodos() {
        log.info("Listando todos los grupos");
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public ResponseGrupo actualizar(Long id, RequestGrupo dto, Long idUsuario) {

        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Grupo no encontrado con id: " + id));

        if (!grupo.getIdCreador().equals(idUsuario)) {
            log.warn("Actualización rechazada: usuario={} no es creador del grupo={}", idUsuario, id);
            throw new RuntimeException("No tienes permiso para editar este grupo");
        }

        grupo.setNombre(dto.getNombre());
        grupo.setDescripcion(dto.getDescripcion());


        Grupo actualizado = repo.save(grupo);
        log.info("Grupo actualizado: id={} por usuario={}", id, idUsuario);
        return mapper.toResponse(actualizado);
    }
    @Transactional
    public void eliminar(Long id, Long idUsuario) {

        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Grupo no encontrado con id: " + id));

        if (!grupo.getIdCreador().equals(idUsuario)) {
            log.warn("Eliminación rechazada: usuario={} no es creador del grupo={}", idUsuario, id);
            throw new RuntimeException("No tienes permiso para eliminar este grupo");
        }

        repo.deleteById(id);
        log.info("Grupo eliminado: id={} por usuario={}", id, idUsuario);
    }
    @Transactional(readOnly = true)
    public ResponseGrupo obtenerPorId(Long id) {
        log.info("Buscando grupo por id={}", id);
        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Grupo no encontrado con id: " + id));
        return mapper.toResponse(grupo);
    }
    @Transactional
    public ResponseGrupo unirseAGrupo(Long id, Long idUsuario) {
        Grupo grupo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Grupo no encontrado con id: " + id));

        if (grupo.getMiembros().contains(idUsuario)) {
            log.warn("Unión rechazada: usuario={} ya es miembro del grupo={}", idUsuario, id);
            throw new RuntimeException("Ya estás registrado en este grupo");
        }

        grupo.getMiembros().add(idUsuario);

        Grupo actualizado = repo.save(grupo);
        log.info("Usuario={} se unió al grupo={}", idUsuario, id);
        return mapper.toResponse(actualizado);
    }
}
