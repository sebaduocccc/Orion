package com.orion.eventos_service.Controller;

import com.orion.eventos_service.Assembler.EventoModelAssembler;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Service.EventoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    @Mock private EventoService service;
    @Mock private EventoModelAssembler assembler;

    @InjectMocks private EventoController controller;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    private EventoResponse buildResponse(Long id) {
        EventoResponse r = new EventoResponse();
        r.setIdEvento(id);
        r.setNombre("Hackathon");
        r.setLugar("Santiago");
        return r;
    }

    @Test
    @DisplayName("verEvento: devuelve el evento mapeado a EntityModel")
    void verEvento_ok() {
        EventoResponse resp = buildResponse(1L);
        when(service.obtenerPorId(1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        EntityModel<EventoResponse> result = controller.verEvento(1L);

        assertNotNull(result);
        assertEquals(resp, result.getContent());
        verify(service).obtenerPorId(1L);
    }

    @Test
    @DisplayName("verEventosGlobales: devuelve una colección con todos los eventos")
    void verEventosGlobales_ok() {
        EventoResponse e1 = buildResponse(1L);
        EventoResponse e2 = buildResponse(2L);
        when(service.obtenerTodos()).thenReturn(List.of(e1, e2));
        when(assembler.toModel(any(EventoResponse.class)))
                .thenReturn(EntityModel.of(e1), EntityModel.of(e2));

        CollectionModel<EntityModel<EventoResponse>> result = controller.verEventosGlobales();

        assertEquals(2, result.getContent().size());
        verify(service).obtenerTodos();
    }

    @Test
    @DisplayName("crear: persiste y responde 201 CREATED")
    void crear_ok() {
        EventoRequest req = EventoRequest.builder()
                .nombre("Hackathon").lugar("Santiago")
                .fecha(LocalDateTime.now().plusDays(3)).build();
        EventoResponse creado = buildResponse(10L);
        when(service.guardar(req, 1L)).thenReturn(creado);
        when(assembler.toModel(creado)).thenReturn(EntityModel.of(creado));

        ResponseEntity<EntityModel<EventoResponse>> result = controller.crear(req, 1L);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(creado, result.getBody().getContent());
        verify(service).guardar(req, 1L);
    }

    @Test
    @DisplayName("unirse: agrega al usuario y responde 200 OK")
    void unirse_ok() {
        EventoResponse resp = buildResponse(7L);
        when(service.unirseAEvento(7L, 5L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        ResponseEntity<EntityModel<EventoResponse>> result = controller.unirse(7L, 5L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).unirseAEvento(7L, 5L);
    }

    @Test
    @DisplayName("actualizar: actualiza y responde 200 OK")
    void actualizar_ok() {
        EventoRequest req = EventoRequest.builder()
                .nombre("Nuevo").lugar("Viña")
                .fecha(LocalDateTime.now().plusDays(2)).build();
        EventoResponse resp = buildResponse(3L);
        when(service.actualizar(3L, req, 1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        ResponseEntity<EntityModel<EventoResponse>> result = controller.actualizar(3L, req, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).actualizar(3L, req, 1L);
    }

    @Test
    @DisplayName("borrar: elimina y responde 204 NO CONTENT")
    void borrar_ok() {
        ResponseEntity<Void> result = controller.borrar(9L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service).eliminar(9L, 1L);
    }
}
