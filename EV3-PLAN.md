# EV3 - Evaluación Parcial 3
## Sistema de Gestión Bibliotecaria - Microservicios
**Estudiante:** Javier Cataldo  
**Asignatura:** Desarrollo Fullstack I  

---

## Descripción

Proyecto backend de microservicios para la gestión de una biblioteca digital.  
Esta evaluación corresponde a la **tercera y última etapa** del desarrollo progresivo del semestre, donde se incorporan:

- API Gateway para comunicación centralizada
- Pruebas unitarias con JUnit 5 y Mockito
- Documentación completa con Postman
- Despliegue del sistema completo

---

## Microservicios

| Microservicio | Puerto | Base de datos | Funcionalidad |
|---|---|---|---|
| `socios-service` | 8081 | `biblioteca_socios` | CRUD de socios |
| `prestamos-service` | 8082 | `biblioteca_prestamos` | Gestión de préstamos |
| `pagos-service` | 8083 | `biblioteca_pagos` | Registro de pagos |
| `reservas-service` | 8084 | `biblioteca_reservas` | Gestión de reservas |
| `multas-service` | 8085 | `biblioteca_multas` | Gestión de multas |

### Comunicación entre servicios

```
socios-service (8081)
      ↑
prestamos-service (8082) → valida socio vía WebClient
      ↑
multas-service (8085) → valida préstamo vía WebClient
      ↑
pagos-service (8083) → valida multa vía WebClient
```

---

## Stack Tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | - |
| Spring Cloud Gateway | - |
| MySQL | 8 |
| Maven | - |
| Lombok | - |
| WebClient | - |
| JUnit 5 + Mockito | - |

---

## Lo Implementado en EV3

### 1. API Gateway

Proyecto Spring Cloud Gateway que unifica el acceso a todos los microservicios a través de un solo puerto (`8080`).

**Rutas configuradas:**

| Ruta | Destino |
|---|---|
| `/api/socios/**` | `socios-service:8081` |
| `/api/prestamos/**` | `prestamos-service:8082` |
| `/api/pagos/**` | `pagos-service:8083` |
| `/api/reservas/**` | `reservas-service:8084` |
| `/api/multas/**` | `multas-service:8085` |

### 2. Testing con JUnit y Mockito

Cada endpoint de cada microservicio cuenta con **2 pruebas unitarias** como mínimo, cubriendo casos de éxito y error:

- **Service Tests:** Mockean los repositorios y prueban la lógica de negocio
- **Controller Tests:** Usan `@WebMvcTest` para probar los endpoints HTTP
- **Integration Tests:** Prueban la comunicación real entre servicios vía WebClient

### 3. Documentación con Postman

Colección completa de Postman exportada (`postman_collection.json`) con todos los endpoints de los 5 microservicios, incluyendo ejemplos de peticiones y respuestas esperadas.

### 4. Despliegue

El sistema se despliega localmente con Docker:

```
docker-compose up --build
```

Esto levanta:
- 1 contenedor MySQL 8
- 5 contenedores con cada microservicio
- 1 contenedor con el API Gateway

---

## Endpoints por Microservicio

### socios-service (8081)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/socios` | Listar todos los socios |
| GET | `/api/socios/{id}` | Obtener socio por ID |
| POST | `/api/socios` | Crear nuevo socio |
| PUT | `/api/socios/{id}` | Actualizar socio |
| DELETE | `/api/socios/{id}` | Eliminar socio |

### prestamos-service (8082)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/prestamos` | Listar préstamos |
| GET | `/api/prestamos/{id}` | Obtener préstamo por ID |
| GET | `/api/prestamos/socio/{socioId}` | Préstamos por socio |
| POST | `/api/prestamos` | Crear préstamo |
| PUT | `/api/prestamos/{id}/devolver` | Devolver libro |

### pagos-service (8083)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/pagos` | Listar pagos |
| GET | `/api/pagos/{id}` | Obtener pago por ID |
| GET | `/api/pagos/socio/{socioId}` | Pagos por socio |
| POST | `/api/pagos` | Registrar pago |

### reservas-service (8084)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Obtener reserva por ID |
| GET | `/api/reservas/socio/{socioId}` | Reservas por socio |
| POST | `/api/reservas` | Crear reserva |
| PUT | `/api/reservas/{id}/cancelar` | Cancelar reserva |

### multas-service (8085)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/multas` | Listar multas |
| GET | `/api/multas/{id}` | Obtener multa por ID |
| GET | `/api/multas/socio/{socioId}` | Multas por socio |
| GET | `/api/multas/pendientes` | Multas pendientes |
| POST | `/api/multas` | Generar multa |
| PUT | `/api/multas/{id}/pagar` | Pagar multa |

---

## Puntos Extras

### Seguridad con JWT (+2)
- Autenticación mediante tokens JWT
- Endpoint `/api/auth/login` para obtener token
- Filtro de validación en cada petición
- Protección de todos los endpoints de los 5 MS

### Logs Descriptivos (+2)
- Log al iniciar cada endpoint: `"Iniciando [método] [ruta]"`
- Log al finalizar: `"Finalizado [método] [ruta] - Resultado: [código estado]"`
- Registro de datos relevantes (IDs, acciones, resultados)

### HATEOAS en GET (+2)
- Implementado en respuestas GET de todos los microservicios
- Links a recursos relacionados (self, socio, préstamo, multa, etc.)
- Navegación entre recursos sin conocer las rutas

---

## Cómo Ejecutar

```bash
# 1. Clonar el repositorio
git clone <repo-url>
cd biblioteca-microservicios

# 2. Levantar todo con Docker
docker-compose up --build

# 3. Acceder por el API Gateway
http://localhost:8080/api/socios
http://localhost:8080/api/prestamos
http://localhost:8080/api/pagos
http://localhost:8080/api/reservas
http://localhost:8080/api/multas
```

---

## Estructura del Proyecto

```
/
├── api-gateway/              # Spring Cloud Gateway (puerto 8080)
├── socios-service/           # Microservicio socios (puerto 8081)
├── prestamos-service/        # Microservicio préstamos (puerto 8082)
├── pagos-service/            # Microservicio pagos (puerto 8083)
├── reservas-service/         # Microservicio reservas (puerto 8084)
├── multas-service/           # Microservicio multas (puerto 8085)
├── docker-compose.yml        # Orquestación Docker
├── postman_collection.json   # Colección Postman
└── README.md                 # Este documento
```
