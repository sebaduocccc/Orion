package com.orion.Grupos_service.Controller;

import com.orion.Grupos_service.Assembler.GrupoModelAssembler;
import com.orion.Grupos_service.Dto.RequestGrupo;
import com.orion.Grupos_service.Dto.ResponseGrupo;
import com.orion.Grupos_service.Service.ServiceGrupo;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoControllerTest {

    @Mock private ServiceGrupo service;
    @Mock private GrupoModelAssembler assembler;

    @InjectMocks private ControllerGrupo controller;

    @BeforeEach
    void setUp() {
        // crearGrupo usa linkTo(...), que necesita un request en contexto
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        SecurityContextHolder.clearContext();
    }

    // El controller hace (Long) auth.getPrincipal() -> el principal debe ser Long
    private void autenticarUsuario(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userId, null));
    }

    private RequestGrupo buildRequest() {
        RequestGrupo req = new RequestGrupo();
        req.setNombre("Devs Duoc");
        req.setDescripcion("Grupo de programación");
        return req;
    }

    private ResponseGrupo buildResponse(Long id) {
        ResponseGrupo r = new ResponseGrupo();
        r.setIdGrupo(id);
        r.setNombre("Devs Duoc");
        return r;
    }

    // ---------- POST crear ----------
    @Test
    @DisplayName("crearGrupo: persiste y responde 201 CREATED")
    void crearGrupo_ok() {
        // given
        autenticarUsuario(1L);
        RequestGrupo req = buildRequest();
        ResponseGrupo creado = buildResponse(10L);
        when(service.guardar(req, 1L)).thenReturn(creado);
        when(assembler.toModel(creado)).thenReturn(EntityModel.of(creado));

        // when
        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.crearGrupo(req);

        // then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(creado, result.getBody().getContent());
        verify(service).guardar(req, 1L);
    }

    // ---------- GET por id ----------
    @Test
    @DisplayName("verGrupos: devuelve el grupo mapeado a EntityModel")
    void verGrupos_ok() {
        // given
        ResponseGrupo resp = buildResponse(1L);
        when(service.obtenerPorId(1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        EntityModel<ResponseGrupo> result = controller.verGrupos(1L);

        // then
        assertNotNull(result);
        assertEquals(resp, result.getContent());
        verify(service).obtenerPorId(1L);
    }

    // ---------- GET todos ----------
    @Test
    @DisplayName("verTodosLosGrupos: devuelve la colección")
    void verTodos_ok() {
        // given
        List<ResponseGrupo> grupos = List.of(buildResponse(1L), buildResponse(2L));
        when(service.obtenerTodos()).thenReturn(grupos);
        when(assembler.toCollectionModel(grupos))
                .thenReturn(CollectionModel.of(List.of(
                        EntityModel.of(buildResponse(1L)),
                        EntityModel.of(buildResponse(2L)))));

        // when
        CollectionModel<EntityModel<ResponseGrupo>> result = controller.verTodosLosGrupos();

        // then
        assertEquals(2, result.getContent().size());
        verify(service).obtenerTodos();
    }

    // ---------- PUT actualizar ----------
    @Test
    @DisplayName("actualizar: actualiza y responde 200 OK")
    void actualizar_ok() {
        // given
        autenticarUsuario(1L);
        RequestGrupo req = buildRequest();
        ResponseGrupo resp = buildResponse(3L);
        when(service.actualizar(3L, req, 1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.actualizar(3L, req);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).actualizar(3L, req, 1L);
    }

    // ---------- POST unirse ----------
    @Test
    @DisplayName("unirse: agrega al usuario y responde 200 OK")
    void unirse_ok() {
        // given
        autenticarUsuario(5L);
        ResponseGrupo resp = buildResponse(7L);
        when(service.unirseAGrupo(7L, 5L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        // when
        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.unirse(7L);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).unirseAGrupo(7L, 5L);
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