# Módulo Administrativo - Sistema de Gestión de Bibliotecas Digitales

## Integrantes
- Vicente Barrera
- Cristóbal Gonzalez
- Javier Cataldo

## Descripción
Microservicios del módulo administrativo del sistema de gestión de bibliotecas digitales, desarrollados con Spring Boot, JPA/Hibernate y MySQL.

## Microservicios
| Microservicio | Puerto | Descripción |
|---|---|---|
| ms-proveedores | 8086 | Gestión de proveedores |
| ms-compras | 8087 | Gestión de compras |
| ms-notificaciones | 8088 | Gestión de notificaciones |
| ms-reportes | 8089 | Gestión de reportes |
| ms-autenticacion | 8090 | Gestión de autenticación y usuarios |

## Tecnologías
- Java 17
- Spring Boot 3.5.14
- JPA + Hibernate
- MySQL 8
- Lombok

## Requisitos previos
- Java 17
- MySQL 8 (Laragon recomendado)
- Maven

## Pasos para ejecutar
1. Iniciar MySQL y crear la base de datos:
```sql
CREATE DATABASE gestion_biblioteca;
```

## Endpoints principales
- GET/POST/PUT/DELETE http://localhost:8086/api/proveedores
- GET/POST/PUT/DELETE http://localhost:8087/api/compras
- GET/POST/PUT/DELETE http://localhost:8088/api/notificaciones
- GET/POST/PUT/DELETE http://localhost:8089/api/reportes
- GET/POST/PUT/DELETE http://localhost:8090/api/usuarios
