package com.orion.eventos_service.Exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String mensaje) {
        super(mensaje);
    }

}
