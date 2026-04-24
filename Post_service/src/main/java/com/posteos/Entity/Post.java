package com.posteos.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true, length = 13,nullable = false)
    private Long userid;
    @NotBlank(message = "CONTENIDO REQUERIDO")
    private String content;
    private String mediaUrl;
    @NotNull
    @Column(nullable = false)
    private Timestamp creado_el;

}
