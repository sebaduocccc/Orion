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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // El controller usa linkTo(...) internamente, que necesita un request en contexto.
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        SecurityContextHolder.clearContext();
    }

    // Simula el usuario autenticado (el controller hace Long.parseLong(auth.getPrincipal().toString()))
    private void autenticarUsuario(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userId.toString(), null));
    }

    private EventoResponse buildResponse(Long id) {
        EventoResponse r = new EventoResponse();
        r.setIdEvento(id);
        r.setNombre("Hackathon");
        r.setLugar("Santiago");
        return r;
    }

    // ---------- GET por id ----------
    @Test
    @DisplayName("verEvento: devuelve el evento mapeado a EntityModel")
    void verEvento_ok() {
        // given
        EventoResponse resp = buildResponse(1L);
        when(service.obtenerPorId(1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        EntityModel<EventoResponse> result = controller.verEvento(1L);

        // then
        assertNotNull(result);
        assertEquals(resp, result.getContent());
        verify(service).obtenerPorId(1L);
    }

    // ---------- GET todos ----------
    @Test
    @DisplayName("verEventosGlobales: devuelve una colección con todos los eventos")
    void verEventosGlobales_ok() {
        // given
        EventoResponse e1 = buildResponse(1L);
        EventoResponse e2 = buildResponse(2L);
        when(service.obtenerTodos()).thenReturn(List.of(e1, e2));
        when(assembler.toModel(any(EventoResponse.class)))
                .thenReturn(EntityModel.of(e1), EntityModel.of(e2));

        // when
        CollectionModel<EntityModel<EventoResponse>> result = controller.verEventosGlobales();

        // then
        assertEquals(2, result.getContent().size());
        verify(service).obtenerTodos();
    }

    // ---------- POST crear ----------
    @Test
    @DisplayName("crear: persiste y responde 201 CREATED")
    void crear_ok() {
        // given
        autenticarUsuario(1L);
        EventoRequest req = EventoRequest.builder()
                .nombre("Hackathon").lugar("Santiago")
                .fecha(LocalDateTime.now().plusDays(3)).build();
        EventoResponse creado = buildResponse(10L);
        when(service.guardar(req, 1L)).thenReturn(creado);
        when(assembler.toModel(creado)).thenReturn(EntityModel.of(creado));

        // when
        ResponseEntity<EntityModel<EventoResponse>> result = controller.crear(req);

        // then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(creado, result.getBody().getContent());
        verify(service).guardar(req, 1L);
    }

    // ---------- POST unirse ----------
    @Test
    @DisplayName("unirse: agrega al usuario y responde 200 OK")
    void unirse_ok() {
        // given
        autenticarUsuario(5L);
        EventoResponse resp = buildResponse(7L);
        when(service.unirseAEvento(7L, 5L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        ResponseEntity<EntityModel<EventoResponse>> result = controller.unirse(7L);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).unirseAEvento(7L, 5L);
    }

    // ---------- PUT actualizar ----------
    @Test
    @DisplayName("actualizar: actualiza y responde 200 OK")
    void actualizar_ok() {

        autenticarUsuario(1L);
        EventoRequest req = EventoRequest.builder()
                .nombre("Nuevo").lugar("Viña")
                .fecha(LocalDateTime.now().plusDays(2)).build();
        EventoResponse resp = buildResponse(3L);
        when(service.actualizar(3L, req, 1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        ResponseEntity<EntityModel<EventoResponse>> result = controller.actualizar(3L, req);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).actualizar(3L, req, 1L);
    }

    // ---------- DELETE borrar ----------
    @Test
    @DisplayName("borrar: elimina y responde 204 NO CONTENT")
    void borrar_ok() {
        // given
        autenticarUsuario(1L);

        // when
        ResponseEntity<Void> result = controller.borrar(9L);

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service).eliminar(9L, 1L);
    }
}