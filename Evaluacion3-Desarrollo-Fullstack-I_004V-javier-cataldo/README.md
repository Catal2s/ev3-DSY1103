# Gestion Biblioteca - EV3

**Estudiante:** Javier Cataldo  
**Asignatura:** Desarrollo Fullstack I (DSY1103)

Sistema de gestión de bibliotecas digitales basado en una arquitectura de microservicios con **Spring Boot 3.4.5**, **Spring Cloud Gateway**, **JPA/Hibernate** y **MySQL**.

Este proyecto corresponde a la **Evaluación Parcial 3 (EV3)** —tercera y última etapa del desarrollo progresivo del semestre— donde se incorporaron:

- API Gateway para comunicación centralizada
- Pruebas unitarias con JUnit 5 y Mockito
- Documentación completa con Postman
- Despliegue del sistema completo con Docker
- HATEOAS en respuestas GET de todos los microservicios
- Logs descriptivos con SLF4J
- Comunicación entre microservicios vía WebClient

---

## Microservicios

| Microservicio | Puerto | Base de datos | Paquete | Descripción |
|---|---|---|---|---|
| socios-service | 8081 | biblioteca_socios | GestionBiblioteca.GestionBiblioteca | CRUD de socios |
| prestamos-service | 8082 | biblioteca_prestamos | com.biblioteca.prestamos_service | Gestión de préstamos |
| pagos-service | 8083 | biblioteca_pagos | com.biblioteca.pagos_service | Registro de pagos |
| reservas-service | 8084 | biblioteca_reservas | com.biblioteca.reservas_service | Gestión de reservas |
| multas-service | 8085 | biblioteca_multas | com.biblioteca.multas_service | Gestión de multas |
| gateway-service | 8080 | — | com.biblioteca.gateway | API Gateway (Spring Cloud Gateway) |

---

## Stack Tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Cloud Gateway | 2024.0.1 |
| Spring Data JPA + Hibernate | — |
| MySQL | 8.0 |
| Maven | — |
| Lombok | — |
| WebClient (Spring WebFlux) | — |
| HATEOAS (Spring Boot Starter HATEOAS) | — |
| JUnit 5 + Mockito | — |
| SLF4J (Logging) | — |
| Swagger/OpenAPI (springdoc-openapi) | 2.8.6 |
| Docker / Docker Compose | — |

---

## Novedades de EV3

### 1. API Gateway
Proyecto Spring Cloud Gateway que unifica el acceso a todos los microservicios a través de un solo puerto (`8080`). Configuración en `application.yml` con rutas, predicados y filtros `StripPrefix=0`.

| Ruta | Destino |
|---|---|
| `/api/socios/**` | `socios-service:8081` |
| `/api/prestamos/**` | `prestamos-service:8082` |
| `/api/pagos/**` | `pagos-service:8083` |
| `/api/reservas/**` | `reservas-service:8084` |
| `/api/multas/**` | `multas-service:8085` |

### 2. Testing con JUnit 5 y Mockito
Cada microservicio cuenta con pruebas unitarias de servicio y controlador, más un test de contexto de aplicación. Total: **98 tests** en todo el proyecto.

| Microservicio | Tests Service | Tests Controller | App Context Test | Total |
|---|---|---|---|---|
| socios-service | 8 | 9 | 1 | 18 |
| prestamos-service | 8 | 9 | 1 | 18 |
| pagos-service | 8 | 8 | 1 | 17 |
| reservas-service | 9 | 10 | 1 | 20 |
| multas-service | 12 | 11 | 1 | 24 |
| gateway-service | — | — | 1 | 1 |

### 3. Documentación con Swagger/OpenAPI
Cada microservicio tiene Swagger UI habilitado gracias a `springdoc-openapi-starter-webmvc-ui`. Acceder a:

| Microservicio | Swagger UI |
|---|---|
| socios-service | `http://localhost:8081/swagger-ui.html` |
| prestamos-service | `http://localhost:8082/swagger-ui.html` |
| pagos-service | `http://localhost:8083/swagger-ui.html` |
| reservas-service | `http://localhost:8084/swagger-ui.html` |
| multas-service | `http://localhost:8085/swagger-ui.html` |

Endpoint OpenAPI spec: `http://localhost:{puerto}/v3/api-docs`

### 4. Documentación con Postman
Colección completa exportada en `postman/GestionBiblioteca.postman_collection.json` con todos los endpoints de los 5 microservicios, incluyendo ejemplos de peticiones y respuestas esperadas.

### 5. Despliegue con Docker
El sistema se despliega completamente con Docker mediante un `Dockerfile` multi-etapa parametrizado con `ARG SERVICE_NAME` y `docker-compose.yml`:

```
docker-compose up --build
```

Esto levanta:
- 5 contenedores MySQL 8 (uno por microservicio, puertos 3307–3311)
- 5 contenedores con cada microservicio (puertos 8081–8085)
- 1 contenedor con el API Gateway (puerto 8080)

### 6. HATEOAS
Implementado en respuestas GET de todos los microservicios mediante `spring-boot-starter-hateoas`. Las respuestas incluyen enlaces `self` y a recursos relacionados.

### 7. Logs Descriptivos
Cada microservicio registra logs detallados con SLF4J al iniciar y finalizar cada operación, incluyendo resultados y códigos de estado.

### 8. Comunicación entre microservicios (WebClient)

```
socios-service (8081)
      ↑
prestamos-service (8082) → valida socio vía WebClient
      ↑
multas-service (8085) → valida préstamo vía WebClient
      ↑
pagos-service (8083) → valida multa vía WebClient y la marca como pagada
```

**Flujo completo de ejemplo (préstamo):**
1. Cliente hace `POST /api/prestamos` con `socioId` y `fechaDevolucion`
2. `prestamos-service` llama a `socios-service:8081/api/socios/{id}` para verificar que el socio está activo
3. Si el socio existe y está activo, se crea el préstamo
4. Si no, se devuelve un error 404

---

## Endpoints por microservicio

### socios-service (8081)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/socios` | Listar todos los socios |
| GET | `/api/socios/{id}` | Obtener socio por ID |
| POST | `/api/socios` | Crear nuevo socio |
| PUT | `/api/socios/{id}` | Actualizar socio |
| DELETE | `/api/socios/{id}` | Eliminar socio |

### prestamos-service (8082)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/prestamos` | Listar préstamos |
| GET | `/api/prestamos/{id}` | Obtener préstamo por ID |
| GET | `/api/prestamos/socio/{socioId}` | Préstamos por socio |
| POST | `/api/prestamos` | Crear préstamo |
| PUT | `/api/prestamos/{id}/devolver` | Devolver libro |

### pagos-service (8083)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/pagos` | Listar pagos |
| GET | `/api/pagos/{id}` | Obtener pago por ID |
| GET | `/api/pagos/socio/{socioId}` | Pagos por socio |
| POST | `/api/pagos` | Registrar pago |

### reservas-service (8084)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Obtener reserva por ID |
| GET | `/api/reservas/socio/{socioId}` | Reservas por socio |
| POST | `/api/reservas` | Crear reserva |
| PUT | `/api/reservas/{id}/cancelar` | Cancelar reserva |

### multas-service (8085)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/multas` | Listar multas |
| GET | `/api/multas/{id}` | Obtener multa por ID |
| GET | `/api/multas/socio/{socioId}` | Multas por socio |
| GET | `/api/multas/pendientes` | Multas pendientes |
| POST | `/api/multas` | Generar multa |
| PUT | `/api/multas/{id}/pagar` | Pagar multa |

---

## DTOs por microservicio

| Microservicio | Request DTO | Response DTO | Error DTO |
|---|---|---|---|
| socios-service | SocioRequestDTO | SocioResponseDTO | ErrorResponseDTO |
| prestamos-service | PrestamoRequestDTO | PrestamoResponseDTO | ErrorResponseDTO |
| pagos-service | PagoRequestDTO | PagoResponseDTO | ErrorResponseDTO |
| reservas-service | — | — | ErrorResponseDTO |
| multas-service | — | — | ErrorResponseDTO |

> **Nota:** `reservas-service` y `multas-service` solo cuentan con `ErrorResponseDTO`; no tienen DTOs específicos de request/response.

---

## Configuración de entorno

### Gateway (`gateway-service/src/main/resources/application.yml`)
```yaml
server:
  port: 8080
spring:
  cloud:
    gateway:
      routes:
        - id: socios-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/socios/**
```

### Microservicios (`application.yml`)
Cada microservicio usa `application.yml` con variables de entorno con valores por defecto:

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/biblioteca_xxx?createDatabaseIfNotExist=true}
    username: ${SPRING_DATASOURCE_USERNAME:root}
    password: ${SPRING_DATASOURCE_PASSWORD:}
server:
  port: ${SERVER_PORT:808x}
```

Los servicios que consumen otros microservicios vía WebClient agregan:
- `prestamos-service`: `socios.service.url: ${SOCIOS_SERVICE_URL:http://localhost:8081}`
- `multas-service`: `prestamos.service.url: ${PRESTAMOS_SERVICE_URL:http://localhost:8082}`
- `pagos-service`: `multas.service.url: ${MULTAS_SERVICE_URL:http://localhost:8085}`

---

## Requisitos previos

- Java 21
- Maven (o usar `mvnw`)
- Docker Desktop (para despliegue completo)
- Laragon o MySQL 8 local (para desarrollo sin Docker)

---

## Cómo ejecutar

### Con Docker (recomendado)

```bash
docker-compose up --build
```

Acceder por el API Gateway:
```
http://localhost:8080/api/socios
http://localhost:8080/api/prestamos
http://localhost:8080/api/pagos
http://localhost:8080/api/reservas
http://localhost:8080/api/multas
```

### Sin Docker (desarrollo local)

1. Iniciar MySQL (Laragon) en puerto 3306
2. Ejecutar cada microservicio en terminales separadas:

```bash
cd socios-service    && ./mvnw spring-boot:run
cd prestamos-service  && ./mvnw spring-boot:run
cd pagos-service      && ./mvnw spring-boot:run
cd reservas-service   && ./mvnw spring-boot:run
cd multas-service     && ./mvnw spring-boot:run
cd gateway-service    && ./mvnw spring-boot:run
```

> Las bases de datos se crean automáticamente gracias a `createDatabaseIfNotExist=true` y `spring.jpa.hibernate.ddl-auto=update`.

### Ejecutar tests

```bash
cd <microservicio> && ./mvnw test
```

---

## Estructura del proyecto

```
/
├── gateway-service/            # Spring Cloud Gateway (puerto 8080)
│   └── src/main/resources/application.yml
├── socios-service/             # Microservicio socios (puerto 8081)
├── prestamos-service/          # Microservicio préstamos (puerto 8082)
├── pagos-service/              # Microservicio pagos (puerto 8083)
├── reservas-service/           # Microservicio reservas (puerto 8084)
├── multas-service/             # Microservicio multas (puerto 8085)
├── docker-compose.yml          # Orquestación Docker (5 MySQL + 6 apps)
├── Dockerfile                  # Build multi-etapa parametrizado
├── postman/                    # Colección Postman
│   └── GestionBiblioteca.postman_collection.json
├── EV3-PLAN.md                 # Plan de evaluación EV3
└── README.md                   # Este documento
```

Cada microservicio sigue el patrón **Controller - Service - Repository** con la siguiente estructura interna:

```
microservicio/
├── src/main/java/
│   ├── com/biblioteca/{ms}/      (o GestionBiblioteca.GestionBiblioteca/)
│   │   ├── controller/           # Controladores REST
│   │   ├── service/              # Lógica de negocio
│   │   ├── repository/           # Acceso a datos (JPA Repository)
│   │   ├── model/                # Entidades JPA
│   │   ├── dto/                  # Objetos de transferencia de datos
│   │   ├── exception/            # Manejador global de errores
│   │   └── config/               # Configuración WebClient (en servicios que consumen)
│   └── resources/
│       └── application.yml
├── src/test/java/               # Pruebas unitarias
└── pom.xml
```

> **Nota sobre paquetes:** `socios-service` usa el paquete `GestionBiblioteca.GestionBiblioteca` (grupo `GestionBiblioteca`), mientras que los demás servicios usan `com.biblioteca.{nombre}`.

> **Nota sobre config/:** Solo `prestamos-service`, `pagos-service` y `multas-service` tienen `WebClientConfig` en su paquete `config/`. `socios-service` y `reservas-service` no consumen otros servicios, por lo que no lo requieren.
