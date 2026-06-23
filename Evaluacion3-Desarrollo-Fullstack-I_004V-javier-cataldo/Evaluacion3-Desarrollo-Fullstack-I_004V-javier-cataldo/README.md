# Sistema de Gestión Bibliotecaria - Microservicios

Sistema de gestión de bibliotecas digitales basado en una arquitectura de microservicios con Spring Boot, JPA/Hibernate y MySQL.

## Microservicios implementados

| Microservicio | Puerto | Base de datos | Descripción |
|--------------|--------|---------------|-------------|
| socios-service | 8081 | biblioteca_socios | CRUD de socios de la biblioteca |
| prestamos-service | 8082 | biblioteca_prestamos | Gestión de préstamos de libros |
| pagos-service | 8083 | biblioteca_pagos | Registro de pagos de multas |
| reservas-service | 8084 | biblioteca_reservas | Gestión de reservas de libros |
| multas-service | 8085 | biblioteca_multas | Gestión de multas por devolución tardía |
| gateway-service | 8080 | - | API Gateway (Spring Cloud Gateway) |

## API Gateway

Todas las rutas están centralizadas en el puerto **8080** a través del API Gateway:

| Ruta | Destino |
|------|---------|
| `GET/POST /api/socios/**` | socios-service:8081 |
| `GET/POST /api/prestamos/**` | prestamos-service:8082 |
| `GET/POST /api/pagos/**` | pagos-service:8083 |
| `GET/POST /api/reservas/**` | reservas-service:8084 |
| `GET/POST /api/multas/**` | multas-service:8085 |

## Documentación Swagger/OpenAPI

Cada microservicio expone su propia interfaz Swagger:

| Microservicio | Swagger UI |
|--------------|------------|
| Gateway | http://localhost:8080/swagger-ui.html |
| Socios | http://localhost:8081/swagger-ui.html |
| Préstamos | http://localhost:8082/swagger-ui.html |
| Pagos | http://localhost:8083/swagger-ui.html |
| Reservas | http://localhost:8084/swagger-ui.html |
| Multas | http://localhost:8085/swagger-ui.html |

## Comunicación entre microservicios (WebClient)

```
prestamos-service (8082) ──GET──► socios-service (8081)
    Valida que el socio existe y está activo

multas-service (8085) ──GET──► prestamos-service (8082)
    Valida que el préstamo existe antes de generar una multa

pagos-service (8083) ──GET──► multas-service (8085)
    Valida que la multa existe antes de registrar un pago
```

## Tecnologías

- Java 21
- Spring Boot 3.4.5
- Spring Cloud Gateway 2024.0.1
- Spring Data JPA + Hibernate
- MySQL 8
- WebClient (comunicación REST)
- Springdoc OpenAPI (Swagger)
- Lombok
- Maven
- Docker

## Ejecución local (sin Docker)

1. Iniciar MySQL en puerto 3306 (Laragon o MySQL standalone)
2. Crear las bases de datos: `biblioteca_socios`, `biblioteca_prestamos`, `biblioteca_pagos`, `biblioteca_reservas`, `biblioteca_multas`
3. Ejecutar cada microservicio en terminales separadas:

```bash
cd socios-service && ./mvnw spring-boot:run
cd prestamos-service && ./mvnw spring-boot:run
cd pagos-service && ./mvnw spring-boot:run
cd reservas-service && ./mvnw spring-boot:run
cd multas-service && ./mvnw spring-boot:run
cd gateway-service && ./mvnw spring-boot:run
```

## Ejecución con Docker

```bash
docker-compose up --build
```

Esto levanta 6 contenedores (5 bases de datos MySQL + 6 microservicios). El Gateway queda disponible en `http://localhost:8080`.

## Endpoints por microservicio

### socios-service (puerto 8081)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/socios | Obtener todos los socios |
| GET | /api/socios/{id} | Obtener socio por ID |
| POST | /api/socios | Crear un nuevo socio |
| PUT | /api/socios/{id} | Actualizar un socio |
| DELETE | /api/socios/{id} | Eliminar un socio |

### prestamos-service (puerto 8082)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/prestamos | Obtener todos los préstamos |
| GET | /api/prestamos/{id} | Obtener préstamo por ID |
| GET | /api/prestamos/socio/{socioId} | Obtener préstamos de un socio |
| POST | /api/prestamos | Crear un nuevo préstamo |
| PUT | /api/prestamos/{id}/devolver | Registrar devolución |

### pagos-service (puerto 8083)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/pagos | Obtener todos los pagos |
| GET | /api/pagos/{id} | Obtener pago por ID |
| GET | /api/pagos/socio/{socioId} | Obtener pagos de un socio |
| POST | /api/pagos | Registrar un nuevo pago |

### reservas-service (puerto 8084)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/reservas | Obtener todas las reservas |
| GET | /api/reservas/{id} | Obtener reserva por ID |
| GET | /api/reservas/socio/{socioId} | Obtener reservas de un socio |
| POST | /api/reservas | Crear una nueva reserva |
| PUT | /api/reservas/{id}/cancelar | Cancelar una reserva |

### multas-service (puerto 8085)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /api/multas | Obtener todas las multas |
| GET | /api/multas/{id} | Obtener multa por ID |
| GET | /api/multas/socio/{socioId} | Obtener multas de un socio |
| GET | /api/multas/pendientes | Obtener multas pendientes |
| POST | /api/multas | Crear una nueva multa |
| PUT | /api/multas/{id}/pagar | Registrar pago de multa |

## Estructura del proyecto

Cada microservicio sigue el patrón CSR (Controller - Service - Repository):

```
microservicio/
├── src/main/java/com/biblioteca/{microservicio}/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   ├── exception/
│   └── config/
├── src/main/resources/
│   └── application.properties
├── src/test/java/
│   ├── controller/
│   └── service/
└── pom.xml
```
