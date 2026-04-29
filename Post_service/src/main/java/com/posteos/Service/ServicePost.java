package com.posteos.Service;
import org.springframework.transaction.annotation.Transactional;
import com.posteos.DTO.PostMapper;
import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Repository.Repository_Post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicePost {

    private final Repository_Post repo;
    private final PostMapper mapper;
    @Transactional
    public PostResponseDTO guardar(PostRequestDTO dto){
        Post post= mapper.aEntidad(dto);
        Post guardado = repo.save(post);
        return mapper.response(guardado);


    }
    @Transactional(readOnly = true)
    public List<PostResponseDTO> buscarUser(Long id){
        List<Post> posts = repo.findByUserid(id);

        List<PostResponseDTO> lista = new ArrayList<>();
        for (Post post : posts){

            lista.add(mapper.response(post));
        }
        return lista;

    }
    @Transactional(readOnly = true)
    public List<PostResponseDTO> obtenerTodos() {
        return repo.findAll()
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }
    @Transactional
    public void eliminar(Long id) {
        Post post = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        repo.deleteById(id);

    }


}
