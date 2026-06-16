package msLibros.msLibros.libros.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class CategoriaClient {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaClient.class);
    private final WebClient webClient;

    public CategoriaClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8082").build();
    }

    public String obtenerNombreCategoria(Long id) {
        try {
            Map response = webClient.get()
                    .uri("/api/categorias/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response != null) {
                return (String) response.get("nombre");
            }
        } catch (Exception e) {
            logger.error("Error al obtener categoria con id {}: {}", id, e.getMessage());
        }
        return "Categoría no disponible";
    }
}