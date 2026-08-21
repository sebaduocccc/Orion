package com.treeaxes.Orion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeRequest {
    private Long id_sender;
    private Long id_receiver;
    private String mensaje;
}
