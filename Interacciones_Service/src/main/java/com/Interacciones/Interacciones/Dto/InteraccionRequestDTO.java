package com.Interacciones.Interacciones.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteraccionRequestDTO {

    @NotNull(message = "El userId es requerido")
    private Long userid;

    @NotNull(message = "El postId es requerido")
    private Long postid;

    @NotBlank(message = "El tipo es requerido")
    private String tipo;
}