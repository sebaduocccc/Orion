package com.orion.Grupos_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGrupo {
    private Long idGrupo;
    private Long idCreador;
    private String nombre;
    private String descripcion;
    private List<String> miembros;
    private LocalDateTime creadoEl;

}