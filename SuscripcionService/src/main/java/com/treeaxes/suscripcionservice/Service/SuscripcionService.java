package com.treeaxes.suscripcionservice.Service;

import com.treeaxes.suscripcionservice.DTO.SuscripcionRequest;
import com.treeaxes.suscripcionservice.Modelo.Plan;
import com.treeaxes.suscripcionservice.Modelo.UsuarioSuscripcion;
import com.treeaxes.suscripcionservice.Repository.PlanRepository;
import com.treeaxes.suscripcionservice.Repository.UsuarioSuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SuscripcionService {

    @Autowired
    private UsuarioSuscripcionRepository usuarioSuscripcionRepository;

    @Autowired
    private PlanRepository planRepository;



    public UsuarioSuscripcion procesarSuscripcion(SuscripcionRequest request) {

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("No existe el plan"));

        // pasarela de pago

        String generarTransaccionId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        UsuarioSuscripcion USub = new UsuarioSuscripcion();
        USub.setUserId(request.getUserId());
        USub.setPlan(plan);
        USub.setFechaInicio(LocalDateTime.now());
        USub.setFechaFin(LocalDateTime.now());
        USub.setEstado("ACTIVA");
        USub.setTransaccionId(generarTransaccionId);

        return usuarioSuscripcionRepository.save(USub);
    }


    public boolean validarEstadoPlus(Long userId){
        return usuarioSuscripcionRepository.findByUserId(userId).stream()
                .anyMatch(sub -> "ACTIVA".equals(sub.getEstado()) && sub.getFechaFin().isAfter(LocalDateTime.now()));
    }

}
