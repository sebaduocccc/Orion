package com.Comentarios.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Orion - Gestión de Comentarios")
                        .version("1.0")
                        .description("Documentación de los endpoints CRUD para el microservicio de comentarios"));
    }

    @Bean
    public OperationCustomizer globalHeaderCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(
                new Parameter()
                    .in("header")
                    .name("X-Auth-User-Id")
                    .required(false)
                    .schema(new StringSchema())
                    .description("ID del usuario autenticado (propagado por el API Gateway)")
            );
            return operation;
        };
    }
}