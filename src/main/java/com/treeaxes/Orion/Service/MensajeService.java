package com.treeaxes.Orion.Service;

import com.treeaxes.Orion.DTO.MensajeRequest;
import com.treeaxes.Orion.DTO.MensajeResponse;
import com.treeaxes.Orion.Model.Mensaje;
import com.treeaxes.Orion.Repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public MensajeResponse crearMensaje(MensajeRequest msj) {
        Mensaje mensaje = new Mensaje();
        mensaje.setId_sender(msj.getId_sender());
        mensaje.setId_receiver(msj.getId_receiver());
        mensaje.setMensaje(msj.getMensaje());
        mensajeRepository.save(mensaje);

        MensajeResponse res = new MensajeResponse(
                mensaje.getId(),
                mensaje.getMensaje(),
                mensaje.getCreatedAt()
        );
        return res;
    }

}