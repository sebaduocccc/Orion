package com.posteos.Repository;


import com.posteos.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    // para obtener todos los post de un usuario ordenado de fecha descendente
    List<Post> findByUseridOrderByCreadoElDesc(Long userid);

}
