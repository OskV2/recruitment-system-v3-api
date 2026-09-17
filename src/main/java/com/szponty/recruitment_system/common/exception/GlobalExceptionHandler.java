package com.szponty.recruitment_system.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

/*
* Reference for Error Responses:
* https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html
* */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create("https://api.toimplement.com/errors/not-found"));
        return pd;
    }

    @ExceptionHandler(InvalidEntityStateException.class)
    public ProblemDetail handleInvalidEntityState(
            InvalidEntityStateException ex
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        pd.setTitle("Invalid entity state");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create(
                "https://api.toimplement.com/errors/invalid-entity-state"
        ));

        return pd;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad request");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create("https://api.toimplement.com/errors/bad-request"));
        return pd;
    }
}
