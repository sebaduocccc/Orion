package com.Comentarios.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "comentario", indexes = {
        @Index(name = "idx_comentario_postid", columnList = "postId"),
        @Index(name = "idx_comentario_userid", columnList = "userId")
})
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 300)
    private String contenido;

    @Column(name = "creado_el", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creadoEl;
}