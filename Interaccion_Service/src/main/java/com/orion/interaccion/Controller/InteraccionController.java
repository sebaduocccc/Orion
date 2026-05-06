package com.orion.interaccion.Controller;


import com.orion.interaccion.Service.InteraccionService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {

    @Autowired
    private InteraccionService interaccionService;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long postId){

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        boolean isLiked = interaccionService.toggleLike(userId,postId);

        return ResponseEntity.ok(isLiked);
    }


    @GetMapping("/post/{postId}/like/count")
    public ResponseEntity<Long> contarLikes(@PathVariable Long postId){
        return ResponseEntity.ok(interaccionService.obtenerTotalLikes(postId));
    }



}
