package com.posteos.Controller;

import com.posteos.Entity.Post;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class ControllerPost {

    @PostMapping
    public String post(@RequestBody Post post,@AuthenticationPrincipal String user){
        return "ingresado posteado por "+ user;
    }

}
