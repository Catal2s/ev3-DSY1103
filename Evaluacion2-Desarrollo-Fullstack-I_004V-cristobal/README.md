# Evaluacion2-Desarrollo-Fullstack-I_004V
## Sistema de Gestión de Bibliotecas Digitales

## Integrantes
- Cristóbal González
- Javier Cataldo
- Vicente Barrera

## Descripción
Sistema de gestión de bibliotecas digitales basado en arquitectura de microservicios con Spring Boot, JPA/Hibernate y MySQL.

## Microservicios
| Microservicio | Puerto | Descripción |
|---|---|---|
| ms-autores | 8081 | Gestión de autores |
| ms-categorias | 8082 | Gestión de categorías |
| ms-editoriales | 8083 | Gestión de editoriales |
| ms-ejemplares | 8084 | Gestión de ejemplares |
| ms-libros | 8085 | Gestión de libros - consume los otros microservicios |

## Tecnologías
- Java 17
- Spring Boot 3.5.14
- JPA + Hibernate
- MySQL 8
- Lombok
- WebClient (comunicación entre microservicios)

## Requisitos previos
- Java 17
- MySQL 8 (Laragon recomendado)
- Maven

## Pasos para ejecutar
1. Iniciar MySQL y crear la base de datos:
```sql
CREATE DATABASE gestion_biblioteca;
```
2. Clonar el repositorio
3. Abrir cada microservicio en VS Code
4. Ejecutar en cada uno:
