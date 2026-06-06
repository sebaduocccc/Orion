#  Orion — Red Social Distribuida

Plataforma social fullstack construida sobre una arquitectura de microservicios. Orion permite publicar posts, interactuar con likes y follows, comentar, chatear en tiempo real y crear eventos, todo orquestado con Docker Compose y enrutado a través de un API Gateway centralizado.

---

## Arquitectura general

Cada dominio tiene su propia base de datos MySQL y responsabilidad bien delimitada. El frontend se comunica exclusivamente con el API Gateway. La autenticación se resuelve con JWT: el `Usuario_Service` emite el token y cada microservicio lo valida de forma independiente.

```
[React + Vite]
      │
      ▼
[API Gateway]  ← Spring Cloud Gateway
      │
      ├──► [Usuario_Service    ]  →  usuario_db
      ├──► [Post_Service       ]  →  feed_db
      ├──► [Comentario_Service ]  →  comentarios_db
      ├──► [Media_Service      ]  →  media_db
      ├──► [Interaccion_Service]  →  interaction_db
      ├──► [Chat_Service       ]  →  chat_db   (WebSocket/STOMP)
      └──► [Eventos_Service    ]  →  eventos_db
```

---

## Tecnologías utilizadas

| Capa | Tecnología | Versión |
|---|---|---|
| Frontend | React | 19.x |
| Bundler | Vite | 8.x |
| Routing frontend | React Router DOM | 7.x |
| WebSocket cliente | STOMP.js + SockJS | 7.x / 1.6.x |
| Backend framework | Spring Boot | 4.0.x |
| API Gateway | Spring Cloud Gateway (WebFlux) | — |
| Lenguaje backend | Java | 21+ |
| Persistencia | MySQL | 8.0 |
| Reducción de boilerplate | Lombok | — |
| Autenticación | JWT  | — |
| HATEOAS | Spring HATEOAS | — |
| Documentación API | Swagger / SpringDoc | — |
| Contenedores | Docker + Docker Compose | — |

---

## Estructura del proyecto

```
Orion-main/
├── docker-compose.yml          # Orquestación de todos los servicios
├── init/init.sql               # Creación inicial de bases de datos
├── orion-app/                  # Frontend React + Vite
├── ApiGateway/                 # Spring Cloud Gateway
├── Usuario_Service/            # Autenticación y gestión de usuarios
├── Post_service/               # Publicaciones del feed
├── Comentario_Service/         # Comentarios por post
├── MediaService/               # Subida y almacenamiento de archivos
├── Interaccion_Service/        # Likes y follows
├── chatService/                # Chat en tiempo real (WebSocket/STOMP)
├── eventos_service/            # Creación y asistencia a eventos
└── media_volumen/              # Volumen local para archivos
```
--- 





## Cómo levantar el proyecto

**Prerrequisitos:** Docker y Docker Compose instalados.

```bash
git clone <url-del-repositorio>
cd Orion-main
docker compose --profile complete up --build
```

> En Windows también está disponible el script `iniciar.bat`.

---

## Base de datos

`init.sql` crea automáticamente todas las bases de datos al iniciar MySQL:

```
usuario_db       → usuarios, roles, permisos, perfiles
feed_db          → posts
comentarios_db   → comentarios
media_db         → archivos multimedia
interaction_db   → likes, follows
chat_db          → mensajes de chat
eventos_db       → eventos y asistentes
```
------------------------------------------
#  Orion — Distributed Social Network

A fullstack social platform built on a microservices architecture. Orion lets users publish posts, interact through likes and follows, comment, chat in real time, and create events — all orchestrated with Docker Compose and routed through a centralized API Gateway.

---

## Architecture

Each domain has its own MySQL database and clearly defined responsibility. The frontend communicates exclusively with the API Gateway. Authentication is handled with JWT: `Usuario_Service` issues the token and each microservice validates it independently.

```
[React + Vite]
      │
      ▼
[API Gateway]  ← Spring Cloud Gateway
      │
      ├──► [Usuario_Service    ]  →  usuario_db
      ├──► [Post_Service       ]  →  feed_db
      ├──► [Comentario_Service ]  →  comentarios_db
      ├──► [Media_Service      ]  →  media_db
      ├──► [Interaccion_Service]  →  interaction_db
      ├──► [Chat_Service       ]  →  chat_db   (WebSocket/STOMP)
      └──► [Eventos_Service    ]  →  eventos_db
```

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Frontend | React | 19.x |
| Bundler | Vite | 8.x |
| Frontend routing | React Router DOM | 7.x |
| WebSocket client | STOMP.js + SockJS | 7.x / 1.6.x |
| Backend framework | Spring Boot | 4.0.x |
| API Gateway | Spring Cloud Gateway (WebFlux) | — |
| Backend language | Java | 21+ |
| Persistence | MySQL | 8.0 |
| Boilerplate reduction | Lombok | — |
| Authentication | JWT | — |
| HATEOAS | Spring HATEOAS | — |
| API docs | Swagger / SpringDoc | — |
| Containers | Docker + Docker Compose | — |

---

## Project Structure

```
Orion-main/
├── docker-compose.yml          # Service orchestration
├── init/init.sql               # Initial database creation
├── orion-app/                  # React + Vite frontend
├── ApiGateway/                 # Single entry point, routing and CORS
├── Usuario_Service/            # JWT auth, profiles, roles and permissions
├── Post_service/               # Main post feed
├── Comentario_Service/         # Per-post comments, Swagger docs
├── MediaService/               # Image and avatar upload/storage
├── Interaccion_Service/        # Likes (toggle) and follows
├── chatService/                # Real-time private chat (WebSocket/STOMP)
├── eventos_service/            # Social events with Spring HATEOAS
└── media_volumen/              # Local volume for media files
```

---

## Getting Started

**Prerequisites:** Docker and Docker Compose installed.

```bash
git clone <repository-url>
cd Orion-main
docker compose --profile complete up --build
```

> On Windows the `iniciar.bat` script is also available.



## Database

`init.sql` automatically creates all databases on MySQL startup:

```
usuario_db       → users, roles, permissions, profiles
feed_db          → posts
comentarios_db   → comments
media_db         → media file records
interaction_db   → likes, follows
chat_db          → chat messages
eventos_db       → events and attendees
```