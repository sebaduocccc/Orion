package com.orion.chatservice.Service;

import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Repository.MensajeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MensajeService {

    public static final Long RECEIVER_GLOBAL = 0L;

    @Autowired
    private MensajeRepository repo;

    public Mensaje guardarMensajePrivado(Mensaje mensaje) {
        if (mensaje.getReceiverId() == null || RECEIVER_GLOBAL.equals(mensaje.getReceiverId())) {
            log.warn("Mensaje privado rechazado: receiverId inválido ({})", mensaje.getReceiverId());
            throw new IllegalArgumentException("Un mensaje privado requiere un receptor válido");
        }
        Mensaje guardado = repo.save(mensaje);
        log.info("Mensaje privado guardado: id={} de usuario={} para usuario={}",
                guardado.getId(), guardado.getSenderId(), guardado.getReceiverId());
        return guardado;
    }

    public Mensaje guardarMensajeGlobal(Mensaje mensaje) {
        mensaje.setReceiverId(RECEIVER_GLOBAL);
        Mensaje guardado = repo.save(mensaje);
        log.info("Mensaje global guardado: id={} emisor={}", guardado.getId(), guardado.getNombreEmisor());
        return guardado;
    }

    public Page<Mensaje> obtenerHistorial(Long user1, Long user2, int page, int size) {
        log.info("Consultando historial privado entre usuario={} y usuario={} (page={}, size={})",
                user1, user2, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return repo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByFechaEnvioDesc(
                user1, user2, user2, user1, pageable);
    }

    public Page<Mensaje> obtenerHistorialGlobal(int page, int size) {
        log.info("Consultando historial global (page={}, size={})", page, size);
        return repo.findByReceiverIdOrderByFechaEnvioDesc(RECEIVER_GLOBAL, PageRequest.of(page, size));
    }
}
