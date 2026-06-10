package com.treeaxes.suscripcionservice.Controller;

import com.treeaxes.suscripcionservice.DTO.SuscripcionRequest;
import com.treeaxes.suscripcionservice.Modelo.UsuarioSuscripcion;
import com.treeaxes.suscripcionservice.Service.SuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    @Autowired
    private SuscripcionService suscripcionService;

    @PostMapping("/adquirir")
    public ResponseEntity<UsuarioSuscripcion> adquirirSuscripcion(@RequestBody SuscripcionRequest request){
        UsuarioSuscripcion nuevaSub = suscripcionService.procesarSuscripcion(request);
        return ResponseEntity.ok(nuevaSub);
    }


    @GetMapping("/estado/{userId}")
    public ResponseEntity<Boolean> verificarSuscripcion(@PathVariable Long userId){
        boolean esPlus = suscripcionService.validarEstadoPlus(userId);
        return ResponseEntity.ok(esPlus);
    }

}
