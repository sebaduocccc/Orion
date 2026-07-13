package com.orion.chatservice.Controller;


import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Service.MensajeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MensajeService mensajeService;


    // react enviara mensajes a la ruta: /app/chat.enviar
    @MessageMapping("/chat.enviar")
    public void procesarMensaje(@Payload Mensaje mensaje) {

        Mensaje mensajeGuardado = mensajeService.guardarMensajePrivado(mensaje);

        // Se le envia en vivo el mensaje al receptor
        // SI el reciverId es '5' esto lo envia al canal especial con el usuario
        messagingTemplate.convertAndSendToUser(
                String.valueOf(mensaje.getReceiverId()),
                "/queue/mensajes",
                mensajeGuardado
        );

        log.info("Mensaje privado enviado a usuario={}", mensaje.getReceiverId());
    }



    @MessageMapping("/chat.global")
    public void procesarGlobalMensaje(@Payload Mensaje mensaje) {
        Mensaje mensajeGuardado = mensajeService.guardarMensajeGlobal(mensaje);
        messagingTemplate.convertAndSend("/topic/publico", mensajeGuardado);
        log.info("Mensaje global enviado por {}", mensaje.getNombreEmisor());
    }



}
