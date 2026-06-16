# Evaluación 3 - Desarrollo Fullstack I
## Sistema de Gestión Bibliotecaria - Microservicios

**Asignatura:** Desarrollo Fullstack I (004V)  
**Semestre:** 2025-2  

---

## Integrantes

| Estudiante | Carpeta | Tema |
|---|---|---|
| Cristobal González | `Evaluacion2-Desarrollo-Fullstack-I_004V-cristobal/` | Catálogo biblioteca (autores, categorías, editoriales, ejemplares, libros) |
| Javier Cataldo | `Evaluacion2-Desarrollo-Fullstack-I_004V-javier-cataldo/` | Gestión de préstamos (socios, préstamos, pagos, reservas, multas) |
| Vicente Barrera | `Evaluacion2-Desarrollo-Fullstack-I_004V-vicente/` | Módulo administrativo (proveedores, compras, notificaciones, reportes, autenticación) |

---

## Proyecto: Cristobal González

**5 microservicios** enfocados en el catálogo de la biblioteca.

| Microservicio | Puerto | Funcionalidad |
|---|---|---|
| `msAutores` | 8081 | CRUD de autores |
| `msCategoria` | 8082 | CRUD de categorías |
| `msEditoriales` | 8083 | CRUD de editoriales |
| `msEjemplares` | 8084 | CRUD de ejemplares |
| `msLibros` | 8085 | CRUD de libros (consume autores, categorías, editoriales vía WebClient) |

**Stack:** Java 17, Spring Boot 3.5.14, MySQL 8, Maven, Lombok  
**Base de datos:** Compartida (`gestion_biblioteca`)

**Pendiente EV3:**
- [ ] API Gateway
- [ ] Testing JUnit + Mockito (2 tests por endpoint)
- [ ] Documentación Postman
- [ ] Despliegue

---

## Proyecto: Javier Cataldo

**5 microservicios** enfocados en la gestión de préstamos y socios.

| Microservicio | Puerto | BD | Funcionalidad |
|---|---|---|---|
| `socios-service` | 8081 | `biblioteca_socios` | CRUD de socios |
| `prestamos-service` | 8082 | `biblioteca_prestamos` | Gestión de préstamos |
| `pagos-service` | 8083 | `biblioteca_pagos` | Registro de pagos |
| `reservas-service` | 8084 | `biblioteca_reservas` | Gestión de reservas |
| `multas-service` | 8085 | `biblioteca_multas` | Gestión de multas |

**Comunicación entre servicios:**
```
socios-service (8081)
      ↑
prestamos-service (8082) → valida socio vía WebClient
      ↑
multas-service (8085) → valida préstamo vía WebClient
      ↑
pagos-service (8083) → valida multa vía WebClient
```

**Stack:** Java 21, Spring Boot 3.4.5, MySQL 8, Maven, Lombok, WebClient  
**Cada MS tiene su propia base de datos.**

**Detalle EV3:** Ver `EV3-PLAN.md` dentro de su carpeta.

---

## Proyecto: Vicente Barrera

**5 microservicios** enfocados en el módulo administrativo de la biblioteca.

| Microservicio | Puerto | Funcionalidad |
|---|---|---|
| `msProveedores` | 8086 | CRUD de proveedores |
| `msCompras` | 8087 | Gestión de compras |
| `msNotificaciones` | 8088 | Gestión de notificaciones (**incompleto**) |
| `msReportes` | 8089 | Generación de reportes |
| `msAutenticacion` | 8090 | Autenticación de usuarios |

**Stack:** Java 17, Spring Boot 3.5.14, MySQL 8, Maven, Lombok  
**Base de datos:** Compartida (`gestion_biblioteca`)  
**Sin comunicación entre servicios** (no usa WebClient)

**Pendiente EV3:**
- [ ] Completar `msNotificaciones` (faltan controller, repository, exception)
- [ ] API Gateway
- [ ] Testing JUnit + Mockito (2 tests por endpoint)
- [ ] Documentación Postman
- [ ] Despliegue

---

## Requisitos Transversales EV3

Cada proyecto debe implementar:

| Requisito | Descripción |
|---|---|
| **API Gateway** | Spring Cloud Gateway que centralice todos los MS en un solo puerto |
| **Testing** | JUnit 5 + Mockito, mínimo 2 pruebas por endpoint |
| **Postman** | Colección completa con todos los endpoints y ejemplos |
| **Despliegue** | Docker + docker-compose (local) o Railway/Render (remoto) |

**Puntos extra:**
- +2: Seguridad con JWT
- +2: Logs descriptivos por endpoint
- +2: HATEOAS en respuestas GET

---

## Stack Común

| Tecnología |
|---|
| Java 17/21 |
| Spring Boot 3.4+ |
| Spring Data JPA / Hibernate |
| MySQL 8 |
| Maven |
| Lombok |
| Docker |
| Postman |
