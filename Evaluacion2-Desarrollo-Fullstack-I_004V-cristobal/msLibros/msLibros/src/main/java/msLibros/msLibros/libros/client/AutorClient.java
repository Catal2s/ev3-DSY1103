package msLibros.msLibros.libros.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class AutorClient {

    private static final Logger logger = LoggerFactory.getLogger(AutorClient.class);
    private final WebClient webClient;

    public AutorClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081").build();
    }

    public String obtenerNombreAutor(Long id) {
        try {
            Map response = webClient.get()
                    .uri("/api/autores/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response != null) {
                return response.get("nombre") + " " + response.get("apellido");
            }
        } catch (Exception e) {
            logger.error("Error al obtener autor con id {}: {}", id, e.getMessage());
        }
        return "Autor no disponible";
    }
}