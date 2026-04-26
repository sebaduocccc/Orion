package com.orion.Usuarios.Config;

import com.orion.Usuarios.Entity.Permiso;
import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Repository.PermisoRepository;
import com.orion.Usuarios.Repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {


    @Bean
    public CommandLineRunner initData(RolRepository rolRepository, PermisoRepository permisoRepository) {

        return args -> {

            if (rolRepository.count() == 0){

                // crear permisos
                Permiso read = new Permiso(); read.setNombre("READ");
                Permiso post = new Permiso(); post.setNombre("POST");
                Permiso delete = new Permiso(); delete.setNombre("DELETE");

                permisoRepository.saveAll(Set.of(read,post,delete));



                // crear rol

                Rol userRole = new Rol();
                userRole.setNombre("ROLE_USER");
                Set<Permiso> userPermisos = new HashSet<>();
                userPermisos.add(read);
                userPermisos.add(post);
                userRole.setPermisos(userPermisos);


                Rol adminRole = new Rol();
                adminRole.setNombre("ROLE_ADMIN");
                Set<Permiso> adminPermisos = new HashSet<>();
                adminPermisos.add(read);
                adminPermisos.add(post);
                adminPermisos.add(delete);
                adminRole.setPermisos(adminPermisos);




                rolRepository.saveAll(Set.of(userRole,adminRole));
                System.out.println("se cargaron los permisos y roles inicialices en la base de datos");
            }
        };

    }

}
