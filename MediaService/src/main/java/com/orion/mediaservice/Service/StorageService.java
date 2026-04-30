package com.orion.mediaservice.Service;


import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Repository.MediaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {


    @Autowired
    private MediaRepository mediaRepository;

    @Value("${media.storage.location}")
    private String storageLocation;

    private Path rootLocation;
    private Path avatarLocation;
    private Path postLocation;


    // se ejecuta al iniciar el microservicio para asegurarse de que exista la carpeta
    @PostConstruct
    public void init(){
        try{
            rootLocation = Paths.get(storageLocation);
            avatarLocation = rootLocation.resolve("avatars");
            postLocation = rootLocation.resolve("posts");

            Files.createDirectories(rootLocation);
            Files.createDirectories(avatarLocation);
            Files.createDirectories(postLocation);

        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta de almacentamiento: \n" + e);
        }

    }


    public Media guardarArchivo(MultipartFile file, Long userId, Media.TipoMedia tipo){

        try{

            if (file.isEmpty()){
                // si esta vacio sale de todo (por excepcion)
                throw new RuntimeException("El archivo esta vacio");
            }

            String nombreOriginal = file.getOriginalFilename();
            String nombreGenerado = UUID.randomUUID().toString()+ "_" + nombreOriginal;

            // ruta donde se guardara fisicamente el media
            Path carpetaDestino = tipo == Media.TipoMedia.AVATAR ? avatarLocation : postLocation;
            Path destinationFile = carpetaDestino.resolve(nombreGenerado).normalize().toAbsolutePath();

            // se copia el archivo de la memoria al disco duro
            Files.copy(file.getInputStream(),destinationFile, StandardCopyOption.REPLACE_EXISTING);


            //se guarda en base de datos
            Media media = new Media();
            media.setUserId(userId);
            media.setTipo(tipo);
            media.setNombreOriginal(nombreOriginal);
            media.setNombreGenerado(nombreGenerado);
            media.setUrlAcceso("/api/media/"+tipo.name().toLowerCase() + "/" + nombreGenerado);

            return mediaRepository.save(media);




        } catch (Exception e){
            System.out.println("fallo al guardar el archivo" +
                    e.getMessage());
        }

        return null;
    }

    // para poder devolver el archivo cuando se solicite
    public Resource cargarArchivo(String nombreGenerado, Media.TipoMedia tipo){

        try{
            Path carpetaOrigen = tipo == Media.TipoMedia.AVATAR ? avatarLocation : postLocation;
            Path file = carpetaOrigen.resolve(nombreGenerado);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new RuntimeException("no se pudo leer el archivo: " + nombreGenerado);
            }

        } catch (MalformedURLException e){
            throw new RuntimeException("Error al cargar el archivo: \n" + nombreGenerado);
        }

    }

}
