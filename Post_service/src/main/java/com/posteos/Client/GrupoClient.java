package com.posteos.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "grupo-client", url = "http://grupos-service:9092/api/grupo")
public interface GrupoClient {

    @GetMapping("/{idGrupo}/miembros")
    List<Long> obtenerMiembros(@PathVariable("idGrupo") Long idGrupo);
}
