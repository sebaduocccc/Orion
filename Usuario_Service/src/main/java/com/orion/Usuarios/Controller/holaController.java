package com.orion.Usuarios.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class holaController {

    @GetMapping("/")
    public String funciona(){
        return "funciona docker";
    }

}
