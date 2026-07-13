# Orion — Red Social Distribuida

## Contexto del proyecto

**Orion** es una red social construida sobre una arquitectura de **microservicios** con Spring Boot. Permite a los usuarios registrarse e iniciar sesión con JWT, publicar posts, comentar, dar likes y follows, subir imágenes, chatear en tiempo real (WebSocket/STOMP), crear grupos y organizar eventos. Todo el ecosistema se orquesta con Docker Compose, se registra en Eureka y se accede a través de un API Gateway centralizado.

## Integrantes del equipo

| Nombre                     | Rol |
|----------------------------|---|
| Sebastián Saavedra         | Desarrollo Fullstack |
| Mateo Nogueira             | Desarrollo Fullstack |

## Microservicios implementados

| # | Microservicio | Puerto | Base de datos | Responsabilidad |
|---|---|---|---|---|
| 1 | ApiGateway | 8000 | — | Punto de entrada único, validación JWT, CORS y enrutamiento |
| 2 | eureka-server | 8761 | — | Registro y descubrimiento de servicios |
| 3 | Usuario_Service | 9090 | usuario_db | Registro, login (emisión JWT), perfiles, roles |
| 4 | Post_service | 9091 | feed_db | Publicaciones del feed |
| 5 | Grupos_service | 9092 | grupos_db | Grupos, membresías (HATEOAS + Feign) |
| 6 | MediaService | 9093 | media_db | Subida y descarga de imágenes/avatares |
| 7 | Interaccion_Service | 9095 | interaction_db | Likes (toggle) y follows |
| 8 | eventos_service | 9096 | eventos_db | Eventos y asistentes (HATEOAS + Feign) |
| 9 | Comentario_Service | 8008 | comentarios_db | Comentarios por post |
| 10 | chatService | 9100 | chat_db | Chat en tiempo real (WebSocket/STOMP) e historial |

Además: **orion-app** (frontend React + Vite) y **MySQL 8** como motor de base de datos (una base por servicio).

## Arquitectura

```
[React + Vite]
      │
      ▼
[API Gateway :8000]  ← Spring Cloud Gateway · valida JWT · CORS
      │
      ├──► [Usuario_Service     :9090]  →  usuario_db
      ├──► [Post_Service        :9091]  →  feed_db
      ├──► [Grupos_Service      :9092]  →  grupos_db      ──Feign──► Usuario_Service
      ├──► [Media_Service       :9093]  →  media_db
      ├──► [Interaccion_Service :9095]  →  interaction_db
      ├──► [Eventos_Service     :9096]  →  eventos_db     ──Feign──► Usuario_Service
      ├──► [Comentario_Service  :8008]  →  comentarios_db
      └──► [Chat_Service        :9100]  →  chat_db   (WebSocket/STOMP)

[Eureka Server :8761]  ← registro y descubrimiento
```

**Seguridad JWT en dos niveles:** el Gateway valida el token en el perímetro, y además **cada microservicio valida el JWT de forma independiente** (filtro `JwtValidationFilter` + Spring Security). La identidad del usuario (`id`, `username`, `roles`) se extrae directamente de los claims del token — no se usan headers intermedios. Grupos y Eventos propagan el token en sus llamadas Feign hacia Usuario_Service.

Cada servicio sigue el patrón **CSR** (Controller–Service–Repository), usa **JPA + Hibernate** con **Liquibase** para migraciones y datos semilla, **Bean Validation** en DTOs, manejo centralizado de errores con `@RestControllerAdvice` y logs estructurados con **SLF4J**.

## Rutas principales del Gateway

| Ruta | Servicio destino |
|---|---|
| `/api/auth/**`, `/api/usuarios/**` | Usuario_Service (`lb://usuarios`) |
| `/api/posts/**` | Post_service (`lb://posteos`) |
| `/api/comentarios/**` | Comentario_Service (`lb://Comentarios`) |
| `/api/media/**` | MediaService (`lb://media-service`) |
| `/api/interacciones/**` | Interaccion_Service |
| `/api/grupo/**` | Grupos_service (`lb://grupos-service`) |
| `/api/evento/**` | eventos_service (`lb://eventos-service`) |
| `/api/chat/**`, `/ws-chat/**` | chatService |

Endpoints públicos (sin token): `/api/auth/login`, `/api/auth/register`, `/api/usuarios/registro`. Todo lo demás requiere `Authorization: Bearer <token>`.

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación en su propio puerto (requiere autorizar con JWT mediante el botón **Authorize**, pegando solo el token):

| Servicio | Swagger UI |
|---|---|
| Usuario_Service | http://localhost:9090/swagger-ui/index.html |
| Post_service | http://localhost:9091/swagger-ui/index.html |
| Grupos_service | http://localhost:9092/swagger-ui/index.html |
| MediaService | http://localhost:9093/swagger-ui/index.html |
| Interaccion_Service | http://localhost:9095/swagger-ui/index.html |
| eventos_service | http://localhost:9096/swagger-ui/index.html |
| Comentario_Service | http://localhost:8008/swagger-ui/index.html |
| chatService | http://localhost:9100/swagger-ui/index.html |

## Ejecución local

**Prerrequisitos:** Docker y Docker Compose.

```bash
git clone <url-del-repositorio>
cd Orion
docker compose up --build
```

Esto levanta MySQL, Eureka, el Gateway y los 8 microservicios. Las bases de datos se crean con `init/init.sql` y los esquemas/datos semilla los aplica Liquibase al arrancar cada servicio.

Flujo de prueba rápido:

```bash
# 1. Registrarse
curl -X POST http://localhost:8000/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234","email":"demo@orion.cl"}'

# 2. Login (devuelve el token JWT)
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234"}'

# 3. Consumir la API con el token
curl http://localhost:8000/api/posts -H "Authorization: Bearer <token>"
```

Para ejecutar las pruebas unitarias de un servicio:

```bash
cd Post_service && ./mvnw test
```

## Ejecución remota (AWS)

El despliegue en AWS (ECR + EC2 con Docker Compose) está documentado paso a paso en [`Plan/DeployAWS.md`](Plan/DeployAWS.md): creación de repositorios ECR, subida de imágenes (incluyendo la alternativa vía CloudShell para cuentas académicas), instancia EC2 t3.large, Security Groups y `docker-compose` de producción.

## Frontend

```bash
cd orion-app
npm install
npm run dev   # http://localhost:5173
```

El frontend consume exclusivamente el Gateway (`http://localhost:8000`) y se conecta al chat vía `ws://localhost:8000/ws-chat`.
