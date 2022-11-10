package hu.bme.aut.viauma06.language_learning.controller.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {

    protected AbstractException(String errorMessage) {
        super(errorMessage);
    }

    public abstract HttpStatus getHttpStatus();
}
