package com.orion.interaccion.Entity;


import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"seguidorId","seguidoId"}) // para no seguir a la misma persona dos veces
})
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long seguidorId;

    @Column(nullable = false)
    private Long seguidoId;

}
