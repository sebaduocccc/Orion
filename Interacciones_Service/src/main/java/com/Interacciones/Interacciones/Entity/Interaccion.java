package com.Interacciones.Interacciones.Entity;



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
@Table(name = "interaccion", indexes = {
        @Index(name = "idx_interaccion_userid", columnList = "userid"),
        @Index(name = "idx_interaccion_postid", columnList = "postid")
})
public class Interaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userid;

    @Column(nullable = false)
    private Long postid;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(name = "creado_el", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creadoEl;
}