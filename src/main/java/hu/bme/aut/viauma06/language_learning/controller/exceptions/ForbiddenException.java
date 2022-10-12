package hu.bme.aut.viauma06.language_learning.controller.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractException {

    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
