package com.orion.Grupos_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "Grupo")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idGrupo;

    @Column(nullable = false)
    private Long idCreador;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @ElementCollection
    @CollectionTable(name = "grupo_miembros", joinColumns = @JoinColumn(name = "grupo_id"))
    @Column(name = "usuario_id")
    private List<Long> miembros;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEl;
}
