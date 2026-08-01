package com.treeaxes.Orion.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class holaController {
    
    @GetMapping("/ola")
    public String hola(){
        return "hola a todo el mundo";
    }

}
