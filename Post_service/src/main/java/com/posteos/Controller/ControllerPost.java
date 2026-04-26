package com.posteos.Controller;

import com.posteos.Entity.Post;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class ControllerPost {

    @PostMapping
    public String post(@RequestBody Post post){
        return "ingresado posteado";
    }

}
