package GestionBiblioteca.GestionBiblioteca.exception;

import GestionBiblioteca.GestionBiblioteca.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

//Esto captura todas las excepciones de los controladores
//Asi devolvemos errores bonitos en vez del stacktrace feo

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Cuando un socio no se encuentra (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        log.error("Error: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //Cuando fallan las validaciones (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Error de validacion: {}", mensaje);
        ErrorResponseDTO error = new ErrorResponseDTO(400, mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
