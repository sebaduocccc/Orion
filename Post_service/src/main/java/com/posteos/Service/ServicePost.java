package com.posteos.Service;

import com.posteos.DTO.PostMapper;
import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Repository.Repository_Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<PostResponseDTO> buscarPostId(Long id){
        // trae todos los posts del usuario de la BD
        List<Post> posts = repo.findByUserid(id);

        // crea una lista vacía donde guardarás los DTOs
        List<PostResponseDTO> lista = new ArrayList<>();
        // recorre cada Post de la lista uno por uno
        for (Post post : posts){

            // convierte ese Post a PostResponseDTO y lo agrega a la lista
            lista.add(mapper.response(post));
        }
        return lista;


    }

}
