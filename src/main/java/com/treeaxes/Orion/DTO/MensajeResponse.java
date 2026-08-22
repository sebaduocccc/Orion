package com.treeaxes.Orion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeResponse {
    private Long id;
    private String mensaje;
    private LocalDateTime createdAt;
}
