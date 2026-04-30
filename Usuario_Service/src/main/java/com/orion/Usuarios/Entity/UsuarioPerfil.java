package com.orion.Usuarios.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_perfil")
public class UsuarioPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // relacion uno a uno con cada usuario
    // cada usuario tiene su unico perfil de usuario

    //el join hace que cree una columna llamada usuario_id y le de el valor de
    // id que es de parte de Usuario usuario.
    @OneToOne
    @JoinColumn(name = "usuario_id",
    referencedColumnName = "id", nullable = false,unique = true)
    private Usuario usuario;


    private String nombreDisplay;


    @Column(length=500)
    private String biografia;

    private String avatarUrl;

    private String ubicacion;


}
