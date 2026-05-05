package com.Comentarios.Service;

import com.Comentarios.DTO.ComentarioMapper;
import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Entity.Comentario;
import com.Comentarios.Exceptions.ResourceNotFoundException;
import com.Comentarios.Repository.RepositoryComentario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceComentario {

    private static final Logger log = LoggerFactory.getLogger(ServiceComentario.class);

    private final RepositoryComentario repo;
    private final ComentarioMapper mapper;

    @Transactional
    public ResponseComentario guardar(RequestComentario dto) {
        log.info("Guardando comentario para post id={}", dto.getPostId());
        Comentario comentario = mapper.aEntidad(dto);
        Comentario guardado = repo.save(comentario);
        log.info("Comentario guardado con id={}", guardado.getId());
        return mapper.response(guardado);
    }

    @Transactional(readOnly = true)
    public List<ResponseComentario> obtenerTodos() {
        log.info("Obteniendo todos los comentarios");
        return repo.findAll()
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseComentario buscarPorId(Long id) {
        log.info("Buscando comentario con id={}", id);
        Comentario comentario = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        return mapper.response(comentario);
    }

    @Transactional
    public ResponseComentario actualizar(Long id, RequestComentario dto) {
        log.info("Actualizando comentario id={}", id);
        Comentario comentario = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        comentario.setContenido(dto.getContenido());
        Comentario actualizado = repo.save(comentario);
        log.info("Comentario id={} actualizado correctamente", id);
        return mapper.response(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando comentario id={}", id);
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        repo.deleteById(id);
        log.info("Comentario id={} eliminado correctamente", id);
    }



    @Transactional(readOnly = true)
    public List<ResponseComentario> buscarPorPost(Long postId) {
        log.info("Buscando comentarios del post id={}", postId);
        return repo.findByPostId(postId)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseComentario> buscarPorUsuario(Long userId) {
        log.info("Buscando comentarios del usuario id={}", userId);
        return repo.findByUserId(userId)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarcomentarios(Long postId) {
        log.info("Contando comentarios del post id={}", postId);
        return repo.countByPostId(postId);
    }
}