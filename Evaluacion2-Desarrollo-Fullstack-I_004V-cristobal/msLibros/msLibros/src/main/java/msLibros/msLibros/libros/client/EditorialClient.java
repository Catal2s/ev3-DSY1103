package msLibros.msLibros.libros.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class EditorialClient {

    private static final Logger logger = LoggerFactory.getLogger(EditorialClient.class);
    private final WebClient webClient;

    public EditorialClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8083").build();
    }

    public String obtenerNombreEditorial(Long id) {
        try {
            Map response = webClient.get()
                    .uri("/api/editoriales/{id}", id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response != null) {
                return (String) response.get("nombre");
            }
        } catch (Exception e) {
            logger.error("Error al obtener editorial con id {}: {}", id, e.getMessage());
        }
        return "Editorial no disponible";
    }
}