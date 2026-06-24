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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoControllerTest {

    @Mock private ServiceGrupo service;
    @Mock private GrupoModelAssembler assembler;

    @InjectMocks private ControllerGrupo controller;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
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

    @Test
    @DisplayName("crearGrupo: persiste y responde 201 CREATED")
    void crearGrupo_ok() {
        RequestGrupo req = buildRequest();
        ResponseGrupo creado = buildResponse(10L);
        when(service.guardar(req, 1L)).thenReturn(creado);
        when(assembler.toModel(creado)).thenReturn(EntityModel.of(creado));

        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.crearGrupo(req, 1L);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(creado, result.getBody().getContent());
        verify(service).guardar(req, 1L);
    }

    @Test
    @DisplayName("verGrupos: devuelve el grupo mapeado a EntityModel")
    void verGrupos_ok() {
        ResponseGrupo resp = buildResponse(1L);
        when(service.obtenerPorId(1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        EntityModel<ResponseGrupo> result = controller.verGrupos(1L);

        assertNotNull(result);
        assertEquals(resp, result.getContent());
        verify(service).obtenerPorId(1L);
    }

    @Test
    @DisplayName("verTodosLosGrupos: devuelve la colección")
    void verTodos_ok() {
        List<ResponseGrupo> grupos = List.of(buildResponse(1L), buildResponse(2L));
        when(service.obtenerTodos()).thenReturn(grupos);
        when(assembler.toCollectionModel(grupos))
                .thenReturn(CollectionModel.of(List.of(
                        EntityModel.of(buildResponse(1L)),
                        EntityModel.of(buildResponse(2L)))));

        CollectionModel<EntityModel<ResponseGrupo>> result = controller.verTodosLosGrupos();

        assertEquals(2, result.getContent().size());
        verify(service).obtenerTodos();
    }

    @Test
    @DisplayName("actualizar: actualiza y responde 200 OK")
    void actualizar_ok() {
        RequestGrupo req = buildRequest();
        ResponseGrupo resp = buildResponse(3L);
        when(service.actualizar(3L, req, 1L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.actualizar(3L, req, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).actualizar(3L, req, 1L);
    }

    @Test
    @DisplayName("unirse: agrega al usuario y responde 200 OK")
    void unirse_ok() {
        ResponseGrupo resp = buildResponse(7L);
        when(service.unirseAGrupo(7L, 5L)).thenReturn(resp);
        when(assembler.toModel(resp)).thenReturn(EntityModel.of(resp));

        ResponseEntity<EntityModel<ResponseGrupo>> result = controller.unirse(7L, 5L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).unirseAGrupo(7L, 5L);
    }

    @Test
    @DisplayName("borrar: elimina y responde 204 NO CONTENT")
    void borrar_ok() {
        ResponseEntity<Void> result = controller.borrar(9L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service).eliminar(9L, 1L);
    }
}
