package com.orion.eventos_service.Service;

import com.orion.eventos_service.DTO.EventoMapper;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Entity.Evento;
import com.orion.eventos_service.Exceptions.ResourceNotFound;
import com.orion.eventos_service.Repository.RepositoryEvento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventoService {
    @Autowired
    private  RepositoryEvento repo;
    @Autowired
    private  EventoMapper mapper;
    @Transactional
    public EventoResponse guardar(EventoRequest dto, Long idCreador) {
        Evento evento = mapper.aEntidad(dto, idCreador);
        Evento guardado = repo.save(evento);
        log.info("Evento creado: id={} nombre='{}' creador={}", guardado.getIdEvento(), guardado.getNombre(), idCreador);
        return mapper.aResponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<EventoResponse> obtenerTodos() {
        log.info("Listando todos los eventos");
        return repo.findAll()
                .stream()
                .map(mapper::aResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public EventoResponse actualizar(Long id, EventoRequest dto, Long idUsuario) {

        Evento evento = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Evento no encontrado con id: " + id));

        if (!evento.getIdCreador().equals(idUsuario)) {
            log.warn("Actualización rechazada: usuario={} no es creador del evento={}", idUsuario, id);
            throw new RuntimeException("No tienes permiso para editar este evento");
        }

        evento.setNombre(dto.getNombre());
        evento.setLugar(dto.getLugar());
        evento.setFecha(dto.getFecha());

        Evento actualizado = repo.save(evento);
        log.info("Evento actualizado: id={} por usuario={}", id, idUsuario);
        return mapper.aResponse(actualizado);
    }
    @Transactional
    public void eliminar(Long id, Long idUsuario) {

        Evento evento = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Evento no encontrado con id: " + id));

        if (!evento.getIdCreador().equals(idUsuario)) {
            log.warn("Eliminación rechazada: usuario={} no es creador del evento={}", idUsuario, id);
            throw new RuntimeException("No tienes permiso para eliminar este evento");
        }

        repo.deleteById(id);
        log.info("Evento eliminado: id={} por usuario={}", id, idUsuario);
    }
    @Transactional(readOnly = true)
    public EventoResponse obtenerPorId(Long id) {
        log.info("Buscando evento por id={}", id);
        Evento evento = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Evento no encontrado con id: " + id));
        return mapper.aResponse(evento);
    }
    @Transactional
    public EventoResponse unirseAEvento(Long idEvento, Long idUsuario) {
        Evento evento = repo.findById(idEvento)
                .orElseThrow(() -> new ResourceNotFound("Evento no encontrado con id: " + idEvento));

        if (evento.getAsistentes().contains(idUsuario)) {
            log.warn("Unión rechazada: usuario={} ya es asistente del evento={}", idUsuario, idEvento);
            throw new RuntimeException("Ya estás registrado en este evento");
        }

        evento.getAsistentes().add(idUsuario);

        Evento actualizado = repo.save(evento);
        log.info("Usuario={} se unió al evento={}", idUsuario, idEvento);
        return mapper.aResponse(actualizado);
    }

}
