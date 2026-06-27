# Sistema de Gestión de Bibliotecas Digitales
**Evaluación EP3 - Desarrollo FullStack I (DSY1103) - DuocUC**

## Integrantes
- Cristóbal Gonzalez
- Javier Cataldo
- Vicente Barrera

## Descripción
Sistema de microservicios para la gestión de una biblioteca digital, desarrollado con Spring Boot 3.5.14 y Spring Cloud 2025.0.3.

## Microservicios
| Servicio | Puerto | Descripción |
|---|---|---|
| msAutores | 8081 | Gestión de autores |
| msCategoria | 8082 | Gestión de categorías |
| msEditoriales | 8083 | Gestión de editoriales |
| msEjemplares | 8084 | Gestión de ejemplares |
| msLibros | 8085 | Gestión de libros |
| msGateway | 8080 | API Gateway |

## Tecnologías
- Java 17, Spring Boot 3.5.14, Spring Cloud 2025.0.3
- MySQL, Docker, JUnit 5 + Mockito, JaCoCo, Swagger/OpenAPI

## Rutas del Gateway (puerto 8080)
| Ruta | Destino |
|---|---|
| /api/autores/** | msAutores:8081 |
| /api/categorias/** | msCategoria:8082 |
| /api/editoriales/** | msEditoriales:8083 |
| /api/ejemplares/** | msEjemplares:8084 |
| /api/libros/** | msLibros:8085 |

## Swagger UI
- http://localhost:8081/swagger-ui/index.html
- http://localhost:8082/swagger-ui/index.html
- http://localhost:8083/swagger-ui/index.html
- http://localhost:8084/swagger-ui/index.html
- http://localhost:8085/swagger-ui/index.html

## Ejecución con Docker
```bash
docker start ms-autores ms-categorias ms-editoriales ms-ejemplares ms-libros
```
