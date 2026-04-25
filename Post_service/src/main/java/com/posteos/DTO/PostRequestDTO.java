package com.posteos.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//PARA RECIBIR DATOS

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {
    @NotNull(message = "El userId es requerido")
    private Long userId;

    @NotBlank(message = "El contenido es requerido")
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String content;

    private String mediaUrl;
}
