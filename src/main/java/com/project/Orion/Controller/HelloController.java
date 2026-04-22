package com.project.Orion.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@PreAuthorize("denyAll()")
public class HelloController {

    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public String HelloGet() {
        return "Hello World";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('POST') or hasAuthority('READ')")
    public String HelloPost() {
        return "Hello World posteado";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
    public String HelloAdmin() {
        return "Hello World admin || developer";
    }
}
