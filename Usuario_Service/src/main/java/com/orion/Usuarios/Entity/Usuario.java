package com.orion.Usuarios.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    // relacion uno a uno con Perfil de usuario
    // si se borra el usuario se borra el perfil de usuario
    // por cascada.
    // por ser LAZY una entidad debil.
    // mappedBy va a la clase UsuarioPerfil y extrae el Usuario usuario(esta misma clase se une)
    @OneToOne(mappedBy = "usuario",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UsuarioPerfil perfil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();



}