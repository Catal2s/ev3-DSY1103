# Gestion Biblioteca - readme.javier_cataldo

Sistema de gestión de bibliotecas digitales basado en una arquitectura de microservicios con Spring Boot, JPA/Hibernate y MySQL (utilizando Laragon), el contenido del readme es acerca de los 5 microservicios creados para la segunda prueba de desarrollo fullstack.

## Microservicios.

| Microservicio  | Puerto | Descripción                                                                    |
| -------------- | ------ | ------------------------------------------------------------------------------ |
| socios-service | 8081   | Gestión de socios (CRUD de miembros de la biblioteca)                          |
| prestamos-service | 8082   | Gestión de préstamos de libros (crear préstamos y registrar devoluciones)      |
| pagos-service  | 8083   | Gestión de pagos de multas (registrar y consultar pagos)                       |
| reservas-service | 8084   | Gestión de reservas de libros (reservar y cancelar reservas)                   |
| multas-service | 8085   | Gestión de multas (generar multas por devolución tardía y registrar pagos)     |

## Tecnologías utilizadas.

- Java 21 # Versión LTS de Java utilizada para el desarrollo, aprovechando características modernas como records, pattern matching y mejora de rendimiento
- Spring Boot 3.4.5
- JPA + Hibernate # ORM (Object-Relational Mapping) que mapea las entidades Java a tablas MySQL, automatizando las operaciones CRUD y consultas
- MySQL 8 (Laragon)
- Bean Validation (JSR 380) # Validación de datos de entrada mediante anotaciones (@Valid, @NotBlank, @Email, etc.) en los DTOs de cada microservicio
- WebClient (comunicación entre microservicios) 
- SLF4J (Logging) # Interfaz de logging para registrar eventos, errores y depuración en la consola de cada microservicio
- Maven # Herramienta de construcción y gestión de dependencias, encargada de compilar, empaquetar y administrar las librerías del proyecto

## Requisitos previos

- Java 21
- Laragon (recomendado)
- Maven

## Pasos para ejecutar Gestion Biblioteca.

1. Iniciar MySQL (Laragon) y asegurarse de que esté corriendo en el puerto 3306. Abrir el GitBash y buscar tu carpeta de preferencia para guardar tu Gestion Biblioteca!

2. Clonar el repositorio:
   ```gitbash
   git clone <url-del-repositorio>
   cd GestionBiblioteca
   ```

3. Abrir cada microservicio en terminales separadas y ejecutar:

   ```gitbash
   # Terminal 1 - Socios
   cd socios-service
   ./mvnw spring-boot:run

   # Terminal 2 - Préstamos
   cd prestamos-service
   ./mvnw spring-boot:run

   # Terminal 3 - Pagos
   cd pagos-service
   ./mvnw spring-boot:run

   # Terminal 4 - Reservas
   cd reservas-service
   ./mvnw spring-boot:run

   # Terminal 5 - Multas
   cd multas-service
   ./mvnw spring-boot:run
   ```

   > **!** Las bases de datos se crean automáticamente gracias a la configuración `createDatabaseIfNotExist=true` y `spring.jpa.hibernate.ddl-auto=update`.

4. Probar los endpoints con Postman | El navegador igual puedes usar para probar los endpoints pero necesario descargar la extension Boomerang en tu navegador de preferencia:

   ```
   GET http://localhost:8081/api/socios
   GET http://localhost:8082/api/prestamos
   GET http://localhost:8083/api/pagos
   GET http://localhost:8084/api/reservas
   GET http://localhost:8085/api/multas
   ```

## Endpoints por microservicio

### socios-service (puerto 8081)

| Método | Ruta                    | Descripción                        |
| ------ | ----------------------- | ---------------------------------- |
| GET    | `/api/socios`           | Obtener todos los socios           |
| GET    | `/api/socios/{id}`      | Obtener socio por ID               |
| POST   | `/api/socios`           | Crear un nuevo socio               |
| PUT    | `/api/socios/{id}`      | Actualizar un socio existente      |
| DELETE | `/api/socios/{id}`      | Eliminar un socio                  |

### prestamos-service (puerto 8082)

| Método | Ruta                            | Descripción                       |
| ------ | ------------------------------- | --------------------------------- |
| GET    | `/api/prestamos`                | Obtener todos los préstamos       |
| GET    | `/api/prestamos/{id}`           | Obtener préstamo por ID           |
| GET    | `/api/prestamos/socio/{socioId}`| Obtener préstamos de un socio     |
| POST   | `/api/prestamos`                | Crear un nuevo préstamo           |
| PUT    | `/api/prestamos/{id}/devolver`  | Registrar devolución de libro     |

### pagos-service (puerto 8083)

| Método | Ruta                       | Descripción                     |
| ------ | -------------------------- | ------------------------------- |
| GET    | `/api/pagos`               | Obtener todos los pagos         |
| GET    | `/api/pagos/{id}`          | Obtener pago por ID             |
| GET    | `/api/pagos/socio/{socioId}`| Obtener pagos de un socio       |
| POST   | `/api/pagos`               | Registrar un nuevo pago         |

### reservas-service (puerto 8084)

| Método | Ruta                           | Descripción                          |
| ------ | ------------------------------ | ------------------------------------ |
| GET    | `/api/reservas`                | Obtener todas las reservas           |
| GET    | `/api/reservas/{id}`           | Obtener reserva por ID               |
| GET    | `/api/reservas/socio/{socioId}`| Obtener reservas de un socio         |
| POST   | `/api/reservas`                | Crear una nueva reserva              |
| PUT    | `/api/reservas/{id}/cancelar`  | Cancelar una reserva                 |

### multas-service (puerto 8085)

| Método | Ruta                         | Descripción                       |
| ------ | ---------------------------- | --------------------------------- |
| GET    | `/api/multas`                | Obtener todas las multas          |
| GET    | `/api/multas/{id}`           | Obtener multa por ID              |
| GET    | `/api/multas/socio/{socioId}`| Obtener multas de un socio        |
| GET    | `/api/multas/pendientes`     | Obtener multas pendientes         |
| POST   | `/api/multas`                | Crear una nueva multa             |
| PUT    | `/api/multas/{id}/pagar`     | Registrar pago de multa           |

## Comunicación entre microservicios (WebClient)

Los microservicios se comunican entre sí mediante **WebClient** (HTTP). Cuando un microservicio necesita datos de otro, realiza una petición GET al endpoint correspondiente:

```
prestamos-service (8082) ──GET──► socios-service (8081)
    Valida que el socio existe y está activo antes de crear un préstamo

multas-service (8085) ──GET──► prestamos-service (8082)
    Valida que el préstamo existe antes de generar una multa

pagos-service (8083) ──GET──► multas-service (8085)
    Valida que la multa existe antes de registrar un pago
```

**Flujo completo de ejemplo (préstamo):**
1. Cliente hace `POST /api/prestamos` con `socioId` y `fechaDevolucion`
2. `prestamos-service` llama a `socios-service:8081/api/socios/{id}` para verificar que el socio está activo
3. Si el socio existe y está activo, se crea el préstamo
4. Si no, se devuelve un error 404 con mensaje descriptivo

## Estructura del proyecto

Cada microservicio sigue el patrón **CSR (Controller - Service - Repository)**:

```
microservicio/
├── src/main/java/com/biblioteca/{microservicio}/
│   ├── controller/     # Manejo de solicitudes REST
│   ├── service/        # Lógica de negocio
│   ├── repository/     # Acceso a datos (JPA)
│   ├── model/          # Entidades JPA
│   ├── dto/            # Objetos de transferencia de datos (request/response)
│   ├── exception/      # Manejador global de errores (@ControllerAdvice)
│   └── config/         # Configuración (WebClient, etc.)
├── src/main/resources/
│   └── application.properties  # Configuración
└── pom.xml             # Dependencias Maven
```
