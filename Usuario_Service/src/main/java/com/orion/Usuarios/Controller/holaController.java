package com.orion.Usuarios.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class holaController {

    @GetMapping("/")
    public String funciona(){
        return "funciona docker";
    }

}
