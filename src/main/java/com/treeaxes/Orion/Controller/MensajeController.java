package com.treeaxes.Orion.Controller;

import com.treeaxes.Orion.Service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;


}
