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

    @ExceptionHandler(InvalidProcessStepStateException.class)
    public ProblemDetail handleInvalidProcessStepState(
            InvalidProcessStepStateException ex
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        pd.setTitle("Invalid process step state");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create(
                "https://api.toimplement.com/errors/invalid-process-step-state"
        ));

        return pd;
    }

}
