package uni.trento.cluster.probe;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleCommandError(IllegalArgumentException e) {
        return e.getMessage();
    }

}
