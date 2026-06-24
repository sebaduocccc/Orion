package com.orion.Grupos_service.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGrupo {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}