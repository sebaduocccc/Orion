package com.orion.apigateway.Security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/usuarios/login",
            "/api/usuarios/register",
            "/api/auth/login",
            "/api/auth/register",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> PUBLIC_ENDPOINTS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
