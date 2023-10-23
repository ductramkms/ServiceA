package com.example.ServiceA.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    /**
     * Handle the others exception, that usually occurs in the internal system.
     *
     * @param exception
     * @return 500 status
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleAllException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.internalServerError().body("Internal Error OK");
    }
}
