package com.treeaxes.Orion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
}

