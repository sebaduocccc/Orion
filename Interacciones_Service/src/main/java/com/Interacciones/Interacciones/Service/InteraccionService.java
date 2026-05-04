package com.Interacciones.Interacciones.Service;

import com.Interacciones.Interacciones.Dto.InteraccionMapper;
import com.Interacciones.Interacciones.Dto.InteraccionRequestDTO;
import com.Interacciones.Interacciones.Dto.InteraccionResponseDTO;
import com.Interacciones.Interacciones.Entity.Interaccion;
import com.Interacciones.Interacciones.Exception.ResourceNotFoundException;
import com.Interacciones.Interacciones.Repository.Repository_Interaccion;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteraccionService {

    private static final Logger log = LoggerFactory.getLogger(InteraccionService.class);

    private final Repository_Interaccion repo;
    private final InteraccionMapper mapper;

    @Transactional
    public InteraccionResponseDTO guardar(InteraccionRequestDTO dto) {
        log.info("Guardando interaccion tipo={} para post id={}", dto.getTipo(), dto.getPostid());
        Interaccion interaccion = mapper.aEntidad(dto);
        Interaccion guardada = repo.save(interaccion);
        log.info("Interaccion guardada con id={}", guardada.getId());
        return mapper.response(guardada);
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las interacciones");
        return repo.findAll()
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InteraccionResponseDTO buscarPorId(Long id) {
        log.info("Buscando interaccion con id={}", id);
        Interaccion interaccion = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaccion no encontrada con id: " + id));
        return mapper.response(interaccion);
    }

    @Transactional
    public InteraccionResponseDTO actualizar(Long id, InteraccionRequestDTO dto) {
        log.info("Actualizando interaccion id={}", id);
        Interaccion interaccion = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaccion no encontrada con id: " + id));
        interaccion.setTipo(dto.getTipo());
        Interaccion actualizada = repo.save(interaccion);
        log.info("Interaccion id={} actualizada correctamente", id);
        return mapper.response(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando interaccion id={}", id);
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaccion no encontrada con id: " + id));
        repo.deleteById(id);
        log.info("Interaccion id={} eliminada correctamente", id);
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> buscarPorUsuario(Long userid) {
        log.info("Buscando interacciones del usuario id={}", userid);
        return repo.findByUserid(userid)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> buscarPorPost(Long postid) {
        log.info("Buscando interacciones del post id={}", postid);
        return repo.findByPostid(postid)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> buscarPorTipo(String tipo) {
        log.info("Buscando interacciones de tipo={}", tipo);
        return repo.findByTipo(tipo)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarPorPostYTipo(Long postid, String tipo) {
        log.info("Contando interacciones tipo={} del post id={}", tipo, postid);
        return repo.countByPostidAndTipo(postid, tipo);
    }
}