package com.orion.eventos_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-service", url = "http://users-service:9090/api/usuarios")
public interface UsuarioClient {
    @GetMapping("/nombre/{id}")
    String obtenerNombrePorId(@PathVariable("id") Long id);
}
