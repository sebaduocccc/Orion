package com.orion.interaccion.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuarioId", "postId"}) // evita que se dé doble like
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long postId;
}
