package com.treeaxes.Orion.Service;

import com.treeaxes.Orion.Model.Mensaje;
import com.treeaxes.Orion.Repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;



}