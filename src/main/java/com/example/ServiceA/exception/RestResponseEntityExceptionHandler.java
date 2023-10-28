package com.example.ServiceA.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    /**
     * Handle the others exception, that usually occurs in the internal system.
     *
     * @param exception
     * @return 500 status
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleAllException(Exception exception) {
        log.warn("AN ERROR HAS OCCURRED");
        return ResponseEntity.internalServerError().body("Internal Error OK");
    }
}
