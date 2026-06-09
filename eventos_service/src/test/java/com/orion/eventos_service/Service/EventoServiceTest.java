package com.orion.eventos_service.Service;

import com.orion.eventos_service.DTO.EventoMapper;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Entity.Evento;
import com.orion.eventos_service.Repository.RepositoryEvento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {
    @Mock
    private RepositoryEvento repo;
    @Mock
    private EventoMapper mapper;
    @InjectMocks
    private EventoService eventoService;
    @Test
    @DisplayName("Deberia guardar un evento y tener una respuesta exitosa")
    void guardarEventoExitosamente() {

        Long id =1L;
        EventoRequest request = new EventoRequest();
        request.setNombre("Hackaton");
        request.setLugar("Israel");
        request.setFecha(LocalDateTime.now().plusDays(3));

        Evento evento= new Evento();
        EventoResponse eventoResponse = new EventoResponse();
        eventoResponse.setNombre("Hackaton");
        when(mapper.aEntidad(request,id)).thenReturn(evento);


    }

}