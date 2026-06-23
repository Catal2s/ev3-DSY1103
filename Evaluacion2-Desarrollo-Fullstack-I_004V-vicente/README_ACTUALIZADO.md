# Registro de cambios - Evaluación Sumativa 3
### DSY1103 - Desarrollo FullStack I

---

## msNotificaciones — implementación completa

El microservicio contaba únicamente con el modelo, el DTO y el servicio. Se agregaron los componentes que faltaban para que fuera funcional:

- Se creó `NotificacionRepository` con los métodos `findByEstado(String estado)` y `findBySocioId(Long socioId)`
- Se creó `NotificacionController` con 7 endpoints REST: listar todas, obtener por id, filtrar por estado, filtrar por socioId, crear, actualizar y eliminar
- Se creó `GlobalExceptionHandler` anotado con `@ControllerAdvice` que maneja `ResourceNotFoundException` (404) y `MethodArgumentNotValidException` (400)
- Se creó `ResourceNotFoundException` como excepción personalizada que el servicio ya referenciaba pero no existía

---

## Swagger / OpenAPI — todos los microservicios

- Se agregó la dependencia `springdoc-openapi-starter-webmvc-ui` versión `2.8.9` al `pom.xml` de los 5 microservicios
- Se anotaron todos los controladores con `@Tag` para identificar el grupo de endpoints en la UI
- Se anotó cada método con `@Operation(summary = ...)` y `@ApiResponses` con los posibles códigos de respuesta (200, 201, 400, 404)
- La documentación queda disponible en `/swagger-ui/index.html` en cada microservicio

---

## Tests unitarios JUnit 5 + Mockito — todos los microservicios

Se crearon clases de test en `src/test/java/.../service/`:

| Microservicio | Clase de test | Tests implementados |
|--------------|--------------|-------------------|
| msAutenticacion | UsuarioServiceTest | 16 |
| msCompras | CompraServiceTest | 14 |
| msNotificaciones | NotificacionServiceTest | 16 |
| msProveedores | ProveedorServiceTest | 12 |
| msReportes | ReporteServiceTest | 14 |

Características de los tests:
- Usan `@ExtendWith(MockitoExtension.class)` — no requieren base de datos ni contexto de Spring
- Se mockea el repositorio con `@Mock` y se inyecta en el servicio con `@InjectMocks`
- Cada endpoint tiene mínimo 2 tests: flujo exitoso y manejo de error (excepción no encontrada)
- Siguen el patrón Given-When-Then
- Incluyen `verify()` para confirmar que el repositorio fue invocado correctamente

Para ejecutar los tests de un microservicio:

```powershell
cd msAutenticacion/msAutenticacion
.\mvnw.cmd test
```

---

## Archivos YAML con perfiles — todos los microservicios

Se creó `application.yml` en `src/main/resources/` de los 5 microservicios. Cada archivo tiene tres secciones separadas con `---`:

**Perfil por defecto (activo: dev):**
- Conexión a MySQL local en `localhost:3306`
- `spring.jpa.show-sql: true`
- `ddl-auto: update`

**Perfil dev:**
- Mismo que por defecto, sin cambios adicionales

**Perfil prod:**
- Conexión usando variables de entorno `${DB_HOST}`, `${DB_USER}`, `${DB_PASSWORD}`
- `spring.jpa.show-sql: false`
- `ddl-auto: validate` (no modifica el esquema en producción)

Cambiar de perfil al ejecutar:
```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## API Gateway — nuevo microservicio

Se creó el módulo `apiGateway/apiGateway/` con las siguientes características:

**Tecnología:** Spring Boot 3.3.5 + Spring Cloud Gateway 2023.0.3

**Puerto:** 8080

**Rutas configuradas en `application.yml`:**

| Ruta de entrada (Gateway) | Microservicio destino |
|--------------------------|----------------------|
| `/api/usuarios/**` | msAutenticacion — `localhost:8090` |
| `/api/compras/**` | msCompras — `localhost:8087` |
| `/api/notificaciones/**` | msNotificaciones — `localhost:8088` |
| `/api/proveedores/**` | msProveedores — `localhost:8086` |
| `/api/reportes/**` | msReportes — `localhost:8089` |

**Otros detalles:**
- CORS habilitado globalmente para todos los orígenes y métodos (GET, POST, PUT, DELETE, OPTIONS)
- Perfil `dev`: URIs apuntan a `localhost`
- Perfil `prod`: URIs usan nombres de contenedor Docker (`ms-autenticacion`, `ms-compras`, etc.)
- Actuator habilitado en `/actuator/health` y `/actuator/gateway`
- El Gateway se levanta con `mvn spring-boot:run` (requiere Maven instalado globalmente) o generando el `mvnw` desde Spring Initializr

Para levantar el Gateway:
```powershell
cd apiGateway/apiGateway
mvn spring-boot:run
```

---

## Colección Postman

Se creó el archivo `Biblioteca_Microservicios.postman_collection.json` en la raíz del proyecto.

- Formato Postman Collection v2.1.0
- Importable directamente desde Postman (File > Import)
- Variables de entorno configuradas por microservicio:
  - `base_autenticacion` → `http://localhost:8090`
  - `base_compras` → `http://localhost:8087`
  - `base_notificaciones` → `http://localhost:8088`
  - `base_proveedores` → `http://localhost:8086`
  - `base_reportes` → `http://localhost:8089`
- Cada endpoint incluye un ejemplo de request body y los posibles responses documentados (200, 201, 400, 404)
- Total: 35 endpoints distribuidos en los 5 microservicios
