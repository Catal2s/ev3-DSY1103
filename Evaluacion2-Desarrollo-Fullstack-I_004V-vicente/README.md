# Módulo Administrativo - Sistema de Gestión de Bibliotecas Digitales
### DSY1103 - Desarrollo FullStack I | Evaluación Sumativa 3

---

## Integrantes

| Nombre | Rol |
|--------|-----|
| Vicente Barrera | Módulo Administrativo |
| Cristóbal González | (módulo asignado) |
| Javier Cataldo | (módulo asignado) |

---

## Descripción

Sistema de gestión para una biblioteca digital que permite administrar usuarios, proveedores, compras de material bibliográfico, reportes del sistema y notificaciones hacia los socios. La arquitectura está basada en microservicios independientes que se comunican a través de un API Gateway centralizado desarrollado con Spring Cloud Gateway.

---

## Microservicios implementados

| Microservicio | Puerto | Descripción |
|--------------|--------|-------------|
| msProveedores | 8086 | Gestión de proveedores (RUT único, activación) |
| msCompras | 8087 | Órdenes de compra — estados: PENDIENTE, APROBADA, RECHAZADA, ENTREGADA |
| msNotificaciones | 8088 | Notificaciones a socios — tipo, estado, socioId |
| msReportes | 8089 | Reportes — tipos: PRESTAMOS, LIBROS, SOCIOS, COMPRAS |
| msAutenticacion | 8090 | Usuarios y roles — ADMIN, BIBLIOTECARIO, SOCIO |
| apiGateway | 8080 | Punto de entrada centralizado hacia todos los microservicios |

---

## Rutas del API Gateway

Todos los microservicios son accesibles desde `http://localhost:8080`:

| Ruta Gateway | Redirige a |
|-------------|-----------|
| `/api/usuarios/**` | msAutenticacion — `localhost:8090` |
| `/api/compras/**` | msCompras — `localhost:8087` |
| `/api/notificaciones/**` | msNotificaciones — `localhost:8088` |
| `/api/proveedores/**` | msProveedores — `localhost:8086` |
| `/api/reportes/**` | msReportes — `localhost:8089` |

---

## Documentación Swagger

Cada microservicio expone su documentación en `/swagger-ui/index.html`:

| Microservicio | Swagger UI |
|--------------|-----------|
| msProveedores | http://localhost:8086/swagger-ui/index.html |
| msCompras | http://localhost:8087/swagger-ui/index.html |
| msNotificaciones | http://localhost:8088/swagger-ui/index.html |
| msReportes | http://localhost:8089/swagger-ui/index.html |
| msAutenticacion | http://localhost:8090/swagger-ui/index.html |

---

## Tecnologías utilizadas

| Tecnología | Versión |
|-----------|---------|
| Java | 17 |
| Spring Boot | 3.5.14 |
| Spring Cloud Gateway | 2023.0.3 |
| Spring Data JPA | - |
| MySQL | 8 |
| Lombok | - |
| springdoc-openapi | 2.8.9 |
| JUnit 5 + Mockito | - |
| Jakarta Validation | - |

---

## Requisitos previos

- Java 17+
- MySQL 8 corriendo en `localhost:3306`
- Maven (o usar el `mvnw` incluido en cada microservicio)
- Laragon o XAMPP (recomendado para MySQL local)

---

## Instrucciones de ejecución local

### 1. Crear la base de datos

```sql
CREATE DATABASE IF NOT EXISTS gestion_biblioteca;
```

### 2. Levantar los microservicios

Abrir una terminal por cada microservicio y ejecutar:

```powershell
# msProveedores — puerto 8086
cd msProveedores/msProveedores
.\mvnw.cmd spring-boot:run

# msCompras — puerto 8087
cd msCompras/msCompras
.\mvnw.cmd spring-boot:run

# msNotificaciones — puerto 8088
cd msNotificaciones/msNotificaciones
.\mvnw.cmd spring-boot:run

# msReportes — puerto 8089
cd msReportes/msReportes
.\mvnw.cmd spring-boot:run

# msAutenticacion — puerto 8090
cd msAutenticacion/msAutenticacion
.\mvnw.cmd spring-boot:run

# apiGateway — puerto 8080 (levantar último)
cd apiGateway/apiGateway
mvn spring-boot:run
```

### 3. Cambiar perfil de ejecución

Todos los microservicios soportan perfiles `dev` y `prod`. Por defecto se ejecutan en `dev`.

```powershell
# Perfil de producción
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Pruebas unitarias

Cada microservicio tiene tests unitarios con JUnit 5 y Mockito que no requieren base de datos.

```powershell
# Ejecutar un test específico
.\mvnw.cmd test -Dtest=UsuarioServiceTest

# Ejecutar todos los tests del microservicio
.\mvnw.cmd test
```

| Microservicio | Archivo de test | Tests |
|--------------|----------------|-------|
| msAutenticacion | UsuarioServiceTest | 16 |
| msCompras | CompraServiceTest | 14 |
| msNotificaciones | NotificacionServiceTest | 16 |
| msProveedores | ProveedorServiceTest | 12 |
| msReportes | ReporteServiceTest | 14 |

Todos siguen el patrón Given-When-Then y cubren flujo exitoso y manejo de errores por endpoint.

---

## Endpoints principales

### msAutenticacion — `localhost:8090`
```
GET    /api/usuarios
GET    /api/usuarios/{id}
GET    /api/usuarios/email/{email}
GET    /api/usuarios/rol/{rol}
GET    /api/usuarios/activos
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### msCompras — `localhost:8087`
```
GET    /api/compras
GET    /api/compras/{id}
GET    /api/compras/proveedor/{proveedorId}
GET    /api/compras/estado/{estado}
POST   /api/compras
PUT    /api/compras/{id}
DELETE /api/compras/{id}
```

### msNotificaciones — `localhost:8088`
```
GET    /api/notificaciones
GET    /api/notificaciones/{id}
GET    /api/notificaciones/estado/{estado}
GET    /api/notificaciones/socio/{socioId}
POST   /api/notificaciones
PUT    /api/notificaciones/{id}
DELETE /api/notificaciones/{id}
```

### msProveedores — `localhost:8086`
```
GET    /api/proveedores
GET    /api/proveedores/activos
GET    /api/proveedores/{id}
POST   /api/proveedores
PUT    /api/proveedores/{id}
DELETE /api/proveedores/{id}
```

### msReportes — `localhost:8089`
```
GET    /api/reportes
GET    /api/reportes/{id}
GET    /api/reportes/tipo/{tipo}
GET    /api/reportes/estado/{estado}
POST   /api/reportes
PUT    /api/reportes/{id}
DELETE /api/reportes/{id}
```

---

## Cambios implementados — Evaluación 3

### msNotificaciones — completado

El microservicio contaba con modelo, DTO y servicio pero le faltaban los componentes de infraestructura:

- Se implementó `NotificacionRepository` con métodos `findByEstado` y `findBySocioId`
- Se implementó `NotificacionController` con los 7 endpoints REST
- Se implementó `GlobalExceptionHandler` para manejo centralizado de errores (404, 400)
- Se implementó `ResourceNotFoundException` referenciada por el servicio existente

### Swagger / OpenAPI

- Se agregó `springdoc-openapi-starter-webmvc-ui:2.8.9` a los 5 `pom.xml`
- Se anotaron todos los controladores con `@Tag`, `@Operation` y `@ApiResponse`
- La UI de Swagger queda disponible en `/swagger-ui/index.html` de cada microservicio

### Pruebas unitarias

- Se crearon clases de test en `src/test/java/.../service/` para cada microservicio
- Se usa `@ExtendWith(MockitoExtension.class)`, `@Mock` en el repositorio y `@InjectMocks` en el servicio
- Se implementaron mínimo 2 tests por endpoint: flujo exitoso y flujo de error
- Se incluyen `verify()` para validar interacciones con el repositorio

### Archivos YAML con perfiles

- Se crearon `application.yml` en los 5 microservicios y en el Gateway
- Perfil `dev`: MySQL local, logs activos, `show-sql: true`
- Perfil `prod`: variables de entorno `${DB_HOST}`, `${DB_USER}`, `${DB_PASSWORD}`, `ddl-auto: validate`

### API Gateway

- Nuevo microservicio `apiGateway` en puerto 8080
- Desarrollado con `spring-cloud-starter-gateway` (Spring Cloud 2023.0.3)
- Rutas configuradas en YAML hacia los 5 microservicios
- Perfil `dev` usa URIs `localhost`, perfil `prod` usa nombres de contenedor Docker
- CORS habilitado globalmente
- Actuator expuesto en `/actuator/health` y `/actuator/gateway`

### Colección Postman

- Archivo `Biblioteca_Microservicios.postman_collection.json` disponible en la raíz
- Importable directamente en Postman
- Incluye variables de entorno por microservicio
- Todos los endpoints con ejemplos de request body y responses (200, 201, 400, 404)
