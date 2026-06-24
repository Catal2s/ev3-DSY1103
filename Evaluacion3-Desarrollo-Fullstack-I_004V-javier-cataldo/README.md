# Gestion Biblioteca - EV3

Sistema de gestión de bibliotecas digitales basado en una arquitectura de microservicios con **Spring Boot 3.4.5**, **Spring Cloud Gateway**, **JPA/Hibernate** y **MySQL**.

Este proyecto corresponde a la **Evaluación Parcial 3 (EV3)** de Desarrollo Fullstack I, donde se incorporaron:

- API Gateway para comunicación centralizada
- Pruebas unitarias con JUnit 5 y Mockito
- Documentación completa con Postman
- Despliegue del sistema completo con Docker
- HATEOAS en respuestas GET de todos los microservicios

---

## Novedades de EV3

### 1. API Gateway
Proyecto Spring Cloud Gateway que unifica el acceso a todos los microservicios a través de un solo puerto (`8080`).

| Ruta | Destino |
|---|---|
| `/api/socios/**` | `socios-service:8081` |
| `/api/prestamos/**` | `prestamos-service:8082` |
| `/api/pagos/**` | `pagos-service:8083` |
| `/api/reservas/**` | `reservas-service:8084` |
| `/api/multas/**` | `multas-service:8085` |

### 2. Testing con JUnit 5 y Mockito
Cada microservicio cuenta con pruebas unitarias y de controlador:

| Microservicio | Tests Service | Tests Controller |
|---|---|---|
| socios-service | 8 tests | 8 tests |
| prestamos-service | 7 tests | 8 tests |
| pagos-service | 6 tests | 7 tests |
| reservas-service | 7 tests | 8 tests |
| multas-service | 9 tests | 10 tests |

### 3. Documentación con Postman
Colección completa exportada en `postman/GestionBiblioteca.postman_collection.json` con todos los endpoints de los 5 microservicios.

### 4. Despliegue con Docker
El sistema se despliega completamente con Docker:

```
docker-compose up --build
```

Esto levanta:
- 5 contenedores MySQL 8 (uno por microservicio)
- 5 contenedores con cada microservicio
- 1 contenedor con el API Gateway

### 5. Logs Descriptivos
Cada microservicio registra logs detallados con SLF4J al iniciar y finalizar cada operación, incluyendo resultados y códigos de estado.

---

## Microservicios

| Microservicio | Puerto | BD | Descripción |
|---|---|---|---|
| socios-service | 8081 | biblioteca_socios | CRUD de socios |
| prestamos-service | 8082 | biblioteca_prestamos | Gestión de préstamos |
| pagos-service | 8083 | biblioteca_pagos | Registro de pagos |
| reservas-service | 8084 | biblioteca_reservas | Gestión de reservas |
| multas-service | 8085 | biblioteca_multas | Gestión de multas |

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Cloud Gateway 2024.0.1
- Spring Data JPA + Hibernate
- MySQL 8
- Maven
- WebClient (comunicación entre microservicios)
- SLF4J (Logging)
- JUnit 5 + Mockito (Testing)
- Lombok
- Docker / Docker Compose

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

## Comunicación entre microservicios (WebClient)

```
socios-service (8081)
      ↑
prestamos-service (8082) → valida socio vía WebClient
      ↑
multas-service (8085) → valida préstamo vía WebClient
      ↑
pagos-service (8083) → valida multa vía WebClient
```

**Flujo completo de ejemplo (préstamo):**
1. Cliente hace `POST /api/prestamos` con `socioId` y `fechaDevolucion`
2. `prestamos-service` llama a `socios-service:8081/api/socios/{id}` para verificar que el socio está activo
3. Si el socio existe y está activo, se crea el préstamo
4. Si no, se devuelve un error 404

---

## Estructura del proyecto

```
/
├── gateway-service/          # Spring Cloud Gateway (puerto 8080)
├── socios-service/           # Microservicio socios (puerto 8081)
├── prestamos-service/        # Microservicio préstamos (puerto 8082)
├── pagos-service/            # Microservicio pagos (puerto 8083)
├── reservas-service/         # Microservicio reservas (puerto 8084)
├── multas-service/           # Microservicio multas (puerto 8085)
├── docker-compose.yml        # Orquestación Docker
├── Dockerfile                # Build multi-etapa
├── postman/                  # Colección Postman
└── README.md                 # Este documento
```

Cada microservicio sigue el patrón **Controller - Service - Repository**:

```
microservicio/
├── src/main/java/com/biblioteca/{ms}/
│   ├── controller/     # Manejo de solicitudes REST
│   ├── service/        # Lógica de negocio
│   ├── repository/     # Acceso a datos (JPA)
│   ├── model/          # Entidades JPA
│   ├── dto/            # Objetos de transferencia de datos
│   ├── exception/      # Manejador global de errores
│   └── config/         # Configuración (WebClient, etc.)
├── src/main/resources/
│   └── application.properties
├── src/test/           # Pruebas unitarias
└── pom.xml
```
