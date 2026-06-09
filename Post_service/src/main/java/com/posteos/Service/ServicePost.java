package com.posteos.Service;

import com.posteos.Client.GrupoClient;
import com.posteos.DTO.PostMapper;
import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Exception.ResourceNotFoundException;
import com.posteos.Repository.Repository_Post;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicePost {

    private static final Logger log = LoggerFactory.getLogger(ServicePost.class);
    private final GrupoClient grupoClient;
    private final Repository_Post repo;
    private final PostMapper mapper;

    // CRUD

    // CREATE

    @Transactional
    public PostResponseDTO guardar(PostRequestDTO dto) {
        log.info("Guardando nuevo post para usuario id={}", dto.getUserId());
        Post post = mapper.aEntidad(dto);
        if (dto.getIdGrupo() != null) {
            List<Long> miembros = grupoClient.obtenerMiembros(dto.getIdGrupo());
            if (!miembros.contains(dto.getUserId())) {
                throw new RuntimeException("No eres miembro de este grupo");
            }
        }
        Post guardado = repo.save(post);
        log.info("Post guardado con id={}", guardado.getId());
        return mapper.response(guardado);
    }


    // READ
    @Transactional(readOnly = true)
    public PostResponseDTO buscarPorId(Long id) {
        log.info("Buscando post con id={}", id);
        Post post = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        return mapper.response(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los posts");
        return repo.findAll()
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }
    public List<PostResponseDTO> obtenerPorGrupo(Long idGrupo) {
        return repo.findByIdGrupo(idGrupo)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> buscarPostDeUsuario(Long userId) {

        List<Post> posts = repo.findByUserIdOrderByCreadoElDesc(userId);

        return posts.stream()
                .map(post -> new PostResponseDTO(
                        post.getId(),
                        post.getUserId(),
                        post.getContent(),
                        post.getMediaUrl(),
                        post.getCreadoEl(),
                        post.getIdGrupo()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> buscarUser(Long id) {
        log.info("Buscando posts del usuario id={}", id);
        return repo.findByUserIdOrderByCreadoElDesc(id)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long totalDePostDeUsuario(Long userId){
        return repo.countByUserId(userId);
    }

    // UPDATE

    @Transactional
    public PostResponseDTO actualizar(Long id, PostRequestDTO dto) {
        log.info("Actualizando post id={}", id);
        Post post = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para editar este post");
        }
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());
        Post actualizado = repo.save(post);
        log.info("Post id={} actualizado correctamente", id);
        return mapper.response(actualizado);
    }


    // DELETE
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando post id={}", id);

        Post post =repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para editar este post");
        }

        repo.deleteById(id);
        log.info("Post id={} eliminado correctamente", id);
    }




    //FRONTEND

    @Transactional
    public Page<Post> cargarFeedPrincipal(int page, int size){

        Pageable pageable = PageRequest.of(page, size);
        return repo.findAllByOrderByCreadoElDesc(pageable);

    }



    @Transactional
    public Page<Post> cargarFeedUsuario(int page, int size, Long userId){

        Pageable pageable = PageRequest.of(page, size);
        return repo.findByUserIdOrderByCreadoElDesc(userId,pageable);

    }





}