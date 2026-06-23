package com.orion.mediaservice.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMedia tipo;


    private String nombreOriginal;

    private String nombreGenerado;

    private String urlAcceso;

    @Column(updatable = false)
    private LocalDateTime subidoEl = LocalDateTime.now();

    public enum TipoMedia{
        AVATAR,
        POST
    }

}
