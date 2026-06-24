# Orion — Red Social Distribuida

Plataforma social fullstack construida sobre una arquitectura de microservicios. Orion permite publicar posts, interactuar con likes y follows, comentar, chatear en tiempo real, crear grupos y organizar eventos, todo orquestado con Docker Compose y enrutado a través de un API Gateway centralizado.

---

## Arquitectura general

Cada dominio tiene su propia base de datos MySQL y responsabilidad bien delimitada. El frontend se comunica exclusivamente con el API Gateway. La autenticación se basa en el patrón **Gateway Offloading**: el `Usuario_Service` emite el token JWT, el Gateway lo valida e inyecta el `X-Auth-User-Id` como header HTTP. Los microservicios **no validan JWT de forma independiente**.

```
[React + Vite]
      │
      ▼
[API Gateway :8000]  ← Spring Cloud Gateway · JWT filter · CORS
      │
      ├──► [Usuario_Service     :9090]  →  usuario_db
      ├──► [Post_Service        :9091]  →  feed_db
      ├──► [Grupos_Service      :9092]  →  grupos_db
      ├──► [Media_Service       :9093]  →  media_db
      ├──► [Interaccion_Service :9095]  →  interaction_db
      ├──► [Eventos_Service     :9096]  →  eventos_db
      ├──► [Comentario_Service  :8008]  →  comentarios_db
      └──► [Chat_Service        :9100]  →  chat_db   (WebSocket/STOMP)

[Eureka Server :8761]  ← registro y descubrimiento de servicios
```

---

## Patrones arquitectónicos aplicados

| Patrón | Descripción |
|---|---|
| **Gateway Offloading** | El JWT se valida una sola vez en el Gateway. Cada request autenticado lleva el header `X-Auth-User-Id` hacia los microservicios. |
| **Service Discovery** | Eureka Server centraliza el registro. La mayoría de rutas del Gateway usan `lb://` (load-balanced). |
| **Database per Service** | Cada microservicio tiene su propia base de datos MySQL aislada. |
| **Schema Migration** | Liquibase gestiona DDL y datos semilla en todos los servicios con base de datos. |
| **HATEOAS** | `Grupos_Service` y `Eventos_Service` devuelven hipervínculos en sus respuestas (Spring HATEOAS). |
| **Feign Client** | `Grupos_Service` y `Eventos_Service` llaman a `Usuario_Service` vía OpenFeign para resolver nombres de miembros/asistentes. |

---

## Tecnologías utilizadas

| Capa | Tecnología | Versión |
|---|---|---|
| Frontend | React | 19.x |
| Bundler | Vite | 8.x |
| Routing frontend | React Router DOM | 7.x |
| WebSocket cliente | STOMP.js + SockJS | 7.x / 1.6.x |
| UI CSS | Bootstrap | 5.x |
| Backend framework | Spring Boot | 4.0.x |
| API Gateway | Spring Cloud Gateway (WebFlux) | 2025.1.x |
| Service Discovery | Spring Cloud Netflix Eureka | 2025.1.x |
| Feign Client | Spring Cloud OpenFeign | 2025.1.x |
| Lenguaje backend | Java | 21+ |
| Persistencia | MySQL | 8.0 |
| Migraciones | Liquibase | 5.x |
| Reducción de boilerplate | Lombok | — |
| Autenticación | JWT (solo en Gateway y Usuario_Service) | — |
| HATEOAS | Spring HATEOAS | — |
| Documentación API | Swagger / SpringDoc | — |
| Contenedores | Docker + Docker Compose | — |

---

## Estructura del proyecto

```
Orion/
├── docker-compose.yml           # Orquestación de todos los servicios
├── .env                         # Variables de entorno (puertos, credenciales, JWT)
├── init/init.sql                # Creación inicial de bases de datos MySQL
├── orion-app/                   # Frontend React + Vite
├── ApiGateway/                  # Spring Cloud Gateway — entrada única, filtro JWT, CORS
├── eureka-server/               # Servidor de descubrimiento Eureka
├── Usuario_Service/             # Autenticación JWT, gestión de usuarios y perfiles
├── Post_service/                # Publicaciones del feed principal
├── Grupos_service/              # Grupos de usuarios con miembros y HATEOAS
├── MediaService/                # Subida y almacenamiento de archivos multimedia
├── Interaccion_Service/         # Likes (toggle) y follows
├── eventos_service/             # Eventos con asistentes, HATEOAS y Feign
├── Comentario_Service/          # Comentarios por post
├── chatService/                 # Chat en tiempo real (WebSocket/STOMP) + historial
└── media_volumen/               # Volumen local para archivos de media
```

---

## Servicios y puertos

| Servicio | Puerto | Base de datos | Eureka | Liquibase | JWT propio |
|---|---|---|---|---|---|
| API Gateway | 8000 | — | cliente | — | Valida (filtro global) |
| Eureka Server | 8761 | — | servidor | — | — |
| Usuario_Service | 9090 | usuario_db | cliente | sí | Emite |
| Post_Service | 9091 | feed_db | cliente | sí | No |
| Grupos_Service | 9092 | grupos_db | cliente | sí | No |
| Media_Service | 9093 | media_db | cliente | sí | No |
| Interaccion_Service | 9095 | interaction_db | cliente | No | No |
| Eventos_Service | 9096 | eventos_db | cliente | sí | No |
| Comentario_Service | 8008 | comentarios_db | cliente | sí | No |
| Chat_Service | 9100 | chat_db | cliente | sí | No |

---

## Flujo de autenticación

```
Cliente  →  POST /api/auth/login  →  Usuario_Service
                                          │
                                    emite JWT (HS256)
                                          │
Cliente  ←  { token: "eyJ..." }  ←───────┘

Cliente  →  GET /api/posts/  [Authorization: Bearer eyJ...]
                  │
            API Gateway
            AuthenticationFilter valida firma JWT
            extrae userId → inyecta header X-Auth-User-Id: 42
                  │
            Post_Service recibe request con X-Auth-User-Id: 42
            (no valida JWT, confía en el Gateway)
```

---

## Cómo levantar el proyecto

**Prerrequisitos:** Docker y Docker Compose instalados.

```bash
git clone <url-del-repositorio>
cd Orion
docker compose up --build
```

> No se usan `--profile` ya que todos los servicios están activos en `docker-compose.yml`.

Para reconstruir solo un servicio:
```bash
docker compose up --build chat-service
```

Para ver los logs en tiempo real:
```bash
docker compose logs -f
```

---

## Variables de entorno (`.env`)

El archivo `.env` en la raíz del proyecto configura todo el stack:

```env
# MySQL
DB_ROOT_PASSWORD=root
DB_NAME_USERS=usuario_db
DB_NAME_POSTS=feed_db
DB_NAME_INTERACCION=interaction_db
DB_NAME_MEDIA=media_db
DB_NAME_EVENTOS=eventos_db
DB_NAME_COMENTARIOS=comentarios_db
DB_NAME_GRUPOS=grupos_db
DB_NAME_CHAT=chat_db

# Puertos
PORT_GATEWAY=8000
PORT_EUREKA=8761
PORT_USER_SERVICE=9090
PORT_POST_SERVICE=9091
PORT_GRUPOS_SERVICE=9092
PORT_MEDIA_SERVICE=9093
PORT_INTERACCION_SERVICE=9095
PORT_EVENTOS_SERVICE=9096
PORT_COMENTARIO_SERVICE=8008
PORT_CHAT_SERVICE=9100

# JWT (solo Gateway y Usuario_Service)
JWT_SECRET=<clave-secreta>
JWT_EXPIRATION_TIME=86400000

# Eureka
EUREKA_DEFAULT_ZONE=http://eureka-server:8761/eureka/
```

---

## Base de datos

`init/init.sql` crea automáticamente todas las bases de datos al iniciar MySQL. El esquema de cada base es gestionado por **Liquibase** dentro de cada microservicio al arrancar.

```
usuario_db       → usuarios, roles, permisos, perfiles
feed_db          → posts
grupos_db        → grupos y miembros
media_db         → registros de archivos multimedia
interaction_db   → likes, follows
eventos_db       → eventos y asistentes
comentarios_db   → comentarios por post
chat_db          → mensajes de chat (privados y globales)
```

---

## Chat en tiempo real

El `chatService` expone un endpoint WebSocket sobre STOMP:

| Destino | Tipo | Descripción |
|---|---|---|
| `/app/chat.global` | Publicar | Envía mensaje al canal global |
| `/app/chat.privado` | Publicar | Envía mensaje privado a un usuario |
| `/topic/publico` | Suscribir | Recibe mensajes del canal global |
| `/user/queue/privado` | Suscribir | Recibe mensajes privados |
| `GET /api/chat/historial` | REST | Historial paginado entre dos usuarios |
| `GET /api/chat/historial/global` | REST | Historial paginado del canal global |

Conexión desde el frontend:
```
ws://localhost:8000/ws-chat/websocket  (vía Gateway)
```

---

## Servicios con Feign Client

`Grupos_Service` y `Eventos_Service` consultan a `Usuario_Service` para resolver los nombres de usuarios asociados a miembros o asistentes:

```
GET /api/usuarios/nombre/{id}  →  Usuario_Service
```

Esto permite que las respuestas de grupos y eventos incluyan el nombre de usuario sin almacenarlo localmente.

---

## Documentación de API (Swagger)

Cada servicio expone su Swagger UI. Accesibles en desarrollo en:

| Servicio | URL |
|---|---|
| Usuario_Service | http://localhost:9090/swagger-ui/index.html |
| Post_Service | http://localhost:9091/swagger-ui/index.html |
| Grupos_Service | http://localhost:9092/swagger-ui/index.html |
| Media_Service | http://localhost:9093/swagger-ui/index.html |
| Interaccion_Service | http://localhost:9095/swagger-ui/index.html |
| Eventos_Service | http://localhost:9096/swagger-ui/index.html |
| Comentario_Service | http://localhost:8008/swagger-ui/index.html |
| Chat_Service | http://localhost:9100/swagger-ui/index.html |

---

---

# Orion — Distributed Social Network

A fullstack social platform built on a microservices architecture. Orion lets users publish posts, interact through likes and follows, comment, chat in real time, create groups, and organize events — all orchestrated with Docker Compose and routed through a centralized API Gateway.

---

## Architecture

Each domain has its own MySQL database and clearly defined responsibility. The frontend communicates exclusively with the API Gateway. Authentication follows the **Gateway Offloading** pattern: `Usuario_Service` issues the JWT token, the Gateway validates it and injects the `X-Auth-User-Id` as an HTTP header. Microservices **do not validate JWT independently**.

```
[React + Vite]
      │
      ▼
[API Gateway :8000]  ← Spring Cloud Gateway · JWT filter · CORS
      │
      ├──► [Usuario_Service     :9090]  →  usuario_db
      ├──► [Post_Service        :9091]  →  feed_db
      ├──► [Grupos_Service      :9092]  →  grupos_db
      ├──► [Media_Service       :9093]  →  media_db
      ├──► [Interaccion_Service :9095]  →  interaction_db
      ├──► [Eventos_Service     :9096]  →  eventos_db
      ├──► [Comentario_Service  :8008]  →  comentarios_db
      └──► [Chat_Service        :9100]  →  chat_db   (WebSocket/STOMP)

[Eureka Server :8761]  ← service registry and discovery
```

---

## Architectural Patterns

| Pattern | Description |
|---|---|
| **Gateway Offloading** | JWT is validated once at the Gateway. Every authenticated request carries the `X-Auth-User-Id` header to downstream services. |
| **Service Discovery** | Eureka Server centralizes registration. Most Gateway routes use `lb://` (load-balanced). |
| **Database per Service** | Each microservice has its own isolated MySQL database. |
| **Schema Migration** | Liquibase manages DDL and seed data across all database-backed services. |
| **HATEOAS** | `Grupos_Service` and `Eventos_Service` return hypermedia links in their responses (Spring HATEOAS). |
| **Feign Client** | `Grupos_Service` and `Eventos_Service` call `Usuario_Service` via OpenFeign to resolve member/attendee names. |

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Frontend | React | 19.x |
| Bundler | Vite | 8.x |
| Frontend routing | React Router DOM | 7.x |
| WebSocket client | STOMP.js + SockJS | 7.x / 1.6.x |
| UI CSS | Bootstrap | 5.x |
| Backend framework | Spring Boot | 4.0.x |
| API Gateway | Spring Cloud Gateway (WebFlux) | 2025.1.x |
| Service Discovery | Spring Cloud Netflix Eureka | 2025.1.x |
| Feign Client | Spring Cloud OpenFeign | 2025.1.x |
| Backend language | Java | 21+ |
| Persistence | MySQL | 8.0 |
| Schema migrations | Liquibase | 5.x |
| Boilerplate reduction | Lombok | — |
| Authentication | JWT (Gateway and Usuario_Service only) | — |
| HATEOAS | Spring HATEOAS | — |
| API docs | Swagger / SpringDoc | — |
| Containers | Docker + Docker Compose | — |

---

## Project Structure

```
Orion/
├── docker-compose.yml           # Full stack orchestration
├── .env                         # Environment variables (ports, credentials, JWT)
├── init/init.sql                # Initial MySQL database creation
├── orion-app/                   # React + Vite frontend
├── ApiGateway/                  # Spring Cloud Gateway — single entry point, JWT filter, CORS
├── eureka-server/               # Eureka service registry
├── Usuario_Service/             # JWT auth, user management and profiles
├── Post_service/                # Main post feed
├── Grupos_service/              # User groups with members and HATEOAS
├── MediaService/                # File upload and media storage
├── Interaccion_Service/         # Likes (toggle) and follows
├── eventos_service/             # Events with attendees, HATEOAS and Feign
├── Comentario_Service/          # Per-post comments
├── chatService/                 # Real-time chat (WebSocket/STOMP) + history
└── media_volumen/               # Local volume for media files
```

---

## Services and Ports

| Service | Port | Database | Eureka | Liquibase | Own JWT |
|---|---|---|---|---|---|
| API Gateway | 8000 | — | client | — | Validates (global filter) |
| Eureka Server | 8761 | — | server | — | — |
| Usuario_Service | 9090 | usuario_db | client | yes | Issues |
| Post_Service | 9091 | feed_db | client | yes | No |
| Grupos_Service | 9092 | grupos_db | client | yes | No |
| Media_Service | 9093 | media_db | client | yes | No |
| Interaccion_Service | 9095 | interaction_db | client | No | No |
| Eventos_Service | 9096 | eventos_db | client | yes | No |
| Comentario_Service | 8008 | comentarios_db | client | yes | No |
| Chat_Service | 9100 | chat_db | client | yes | No |

---

## Authentication Flow

```
Client  →  POST /api/auth/login  →  Usuario_Service
                                          │
                                    issues JWT (HS256)
                                          │
Client  ←  { token: "eyJ..." }  ←────────┘

Client  →  GET /api/posts/  [Authorization: Bearer eyJ...]
                  │
            API Gateway
            AuthenticationFilter validates JWT signature
            extracts userId → injects header X-Auth-User-Id: 42
                  │
            Post_Service receives request with X-Auth-User-Id: 42
            (does not validate JWT, trusts the Gateway)
```

---

## Getting Started

**Prerequisites:** Docker and Docker Compose installed.

```bash
git clone <repository-url>
cd Orion
docker compose up --build
```

> No `--profile` flags needed — all services are active in `docker-compose.yml`.

To rebuild a single service:
```bash
docker compose up --build chat-service
```

To stream logs:
```bash
docker compose logs -f
```

---

## Real-time Chat

`chatService` exposes a WebSocket endpoint over STOMP:

| Destination | Type | Description |
|---|---|---|
| `/app/chat.global` | Publish | Send a message to the global channel |
| `/app/chat.privado` | Publish | Send a private message to a user |
| `/topic/publico` | Subscribe | Receive messages from the global channel |
| `/user/queue/privado` | Subscribe | Receive private messages |
| `GET /api/chat/historial` | REST | Paginated history between two users |
| `GET /api/chat/historial/global` | REST | Paginated global channel history |

Frontend connection:
```
ws://localhost:8000/ws-chat/websocket  (via Gateway)
```

---

## Database

`init/init.sql` automatically creates all databases on MySQL startup. Each database schema is managed by **Liquibase** inside each microservice at boot time.

```
usuario_db       → users, roles, permissions, profiles
feed_db          → posts
grupos_db        → groups and members
media_db         → media file records
interaction_db   → likes, follows
eventos_db       → events and attendees
comentarios_db   → per-post comments
chat_db          → chat messages (private and global)
```

---

## API Documentation (Swagger)

Each service exposes its own Swagger UI, accessible during local development at:

| Service | URL |
|---|---|
| Usuario_Service | http://localhost:9090/swagger-ui/index.html |
| Post_Service | http://localhost:9091/swagger-ui/index.html |
| Grupos_Service | http://localhost:9092/swagger-ui/index.html |
| Media_Service | http://localhost:9093/swagger-ui/index.html |
| Interaccion_Service | http://localhost:9095/swagger-ui/index.html |
| Eventos_Service | http://localhost:9096/swagger-ui/index.html |
| Comentario_Service | http://localhost:8008/swagger-ui/index.html |
| Chat_Service | http://localhost:9100/swagger-ui/index.html |
