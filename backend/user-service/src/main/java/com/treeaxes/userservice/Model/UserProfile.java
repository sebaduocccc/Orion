package com.treeaxes.userservice.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // relacion uno a uno con cada usuario
    // cada usuario tiene su unico perfil de usuario

    //el join hace que cree una columna llamada usuario_id y le de el valor de
    // id que es de parte de Usuario usuario.
    @OneToOne
    @JoinColumn(name = "users_id",
    referencedColumnName = "id", nullable = false, unique = true)
    private User user;


    private String displayName;

    @Column(length = 500)
    private String bio;

    private String avatarUrl;

    private String ubicacion;
}
