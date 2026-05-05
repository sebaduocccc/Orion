package com.Comentarios.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestComentario {

    @NotNull(message = "El postId es requerido")
    private Long postId;

    @NotNull(message = "El userId es requerido")
    private Long userId;

    @NotBlank(message = "El contenido es requerido")
    @Size(max = 300, message = "Máximo 300 caracteres")
    private String contenido;
}