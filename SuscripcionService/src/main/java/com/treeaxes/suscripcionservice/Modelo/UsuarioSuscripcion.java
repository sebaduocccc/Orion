package com.treeaxes.suscripcionservice.Modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario_suscripcion")
public class UsuarioSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fechaFin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "transaccion_id", unique = true)
    private String transaccionId;

}
