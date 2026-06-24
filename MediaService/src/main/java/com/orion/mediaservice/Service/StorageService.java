package com.orion.mediaservice.Service;

import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Repository.MediaRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

@Slf4j
@Service
public class StorageService {

    @Autowired
    private MediaRepository mediaRepository;

    @Value("${media.storage.location}")
    private String storageLocation;

    private Path rootLocation;
    private Path avatarLocation;
    private Path postLocation;


    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(storageLocation);
            avatarLocation = rootLocation.resolve("avatars");
            postLocation = rootLocation.resolve("posts");

            Files.createDirectories(rootLocation);
            Files.createDirectories(avatarLocation);
            Files.createDirectories(postLocation);
            log.info("Directorio de almacenamiento inicializado en: {}", rootLocation.toAbsolutePath());
        } catch (IOException e) {
            log.error("No se pudo inicializar el directorio de almacenamiento: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar la carpeta de almacenamiento", e);
        }
    }


    public Media guardarArchivo(MultipartFile file, Long userId, Media.TipoMedia tipo) {
        log.info("Guardando archivo tipo={} para usuario id={}", tipo, userId);
        try {
            if (file.isEmpty()) {
                log.warn("Intento de subida con archivo vacío por usuario id={}", userId);
                throw new RuntimeException("El archivo está vacío");
            }

            String nombreOriginal = file.getOriginalFilename();
            String nombreGenerado = UUID.randomUUID().toString() + "_" + nombreOriginal;

            Path carpetaDestino = tipo == Media.TipoMedia.AVATAR ? avatarLocation : postLocation;
            Path destinationFile = carpetaDestino.resolve(nombreGenerado).normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media();
            media.setUserId(userId);
            media.setTipo(tipo);
            media.setNombreOriginal(nombreOriginal);
            media.setNombreGenerado(nombreGenerado);
            media.setUrlAcceso("/api/media/" + tipo.name().toLowerCase() + "/" + nombreGenerado);

            Media guardado = mediaRepository.save(media);
            log.info("Archivo guardado exitosamente con id={} para usuario id={}", guardado.getId(), userId);
            return guardado;

        } catch (Exception e) {
            log.error("Error al guardar el archivo para usuario id={}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }


    public Resource cargarArchivo(String nombreGenerado, Media.TipoMedia tipo) {
        log.info("Cargando archivo tipo={}: {}", tipo, nombreGenerado);
        try {
            Path carpetaOrigen = tipo == Media.TipoMedia.AVATAR ? avatarLocation : postLocation;
            Path file = carpetaOrigen.resolve(nombreGenerado);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.warn("Archivo no encontrado o no legible: {}", nombreGenerado);
                throw new RuntimeException("No se pudo leer el archivo: " + nombreGenerado);
            }
        } catch (MalformedURLException e) {
            log.error("URL malformada al cargar archivo {}: {}", nombreGenerado, e.getMessage(), e);
            throw new RuntimeException("Error al cargar el archivo: " + nombreGenerado, e);
        }
    }
}
