package hu.bme.aut.viauma06.language_learning.controller;

import hu.bme.aut.viauma06.language_learning.controller.exceptions.AbstractException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<String> handleExceptions(AbstractException e) {
        return new ResponseEntity(e.getMessage(), e.getHttpStatus());
    }
}
