package com.orion.mediaservice.Config;

import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MediaRepository mediaRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (mediaRepository.count() == 0) {
            Media media = new Media();
            media.setNombreGenerado("default_avatar.png");
            media.setNombreOriginal("default_avatar.png");
            media.setTipo(Media.TipoMedia.AVATAR);
            media.setUrlAcceso("/api/media/default_avatar.png");
            media.setUserId(null);
            mediaRepository.saveAll(Set.of(media));
        }
    }
}
