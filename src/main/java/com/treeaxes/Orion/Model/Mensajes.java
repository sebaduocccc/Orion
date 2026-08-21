package com.treeaxes.Orion.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mensajes")
public class Mensajes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long id_sender;

    @Column(unique = true, nullable = false)
    private String id_receiver;

    @Column(nullable = false)
    private String message;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
