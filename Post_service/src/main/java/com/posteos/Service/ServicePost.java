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
    @Transactional(readOnly =true)
    public Post buscarPostId(Long id){
        Post post = ;


    }

}
