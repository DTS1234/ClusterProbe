package uni.trento.cluster.probe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleCommandError(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleFileError(IllegalStateException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
