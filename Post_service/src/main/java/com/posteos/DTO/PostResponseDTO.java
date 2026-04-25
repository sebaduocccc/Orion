package com.posteos.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// PARA DEVOLVER DATOS //
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostResponseDTO {
    private Long id;
    private Long userid;
    private String content;
    private String mediaUrl;
    private LocalDateTime creado_el;

}
