package com.treeaxes.Orion.Controller;

import com.treeaxes.Orion.Model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @GetMapping("/alo")
    public String alo() {
        return "Hola Buenaaassss";
    }

}
