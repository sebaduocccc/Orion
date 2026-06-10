package com.treeaxes.suscripcionservice.Modelo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "planes")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "duration_days", nullable = false)
    private int duracionDias;

}
