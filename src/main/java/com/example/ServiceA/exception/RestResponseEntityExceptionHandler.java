package com.example.ServiceA.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(
            RestResponseEntityExceptionHandler.class);

    /**
     * Handle the others exception, that usually occurs in the internal system.
     *
     * @param exception
     * @return 500 status
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleAllException(Exception exception) {
        logger.warn("AN ERROR HAS OCCURRED");
        return ResponseEntity.internalServerError().body("Internal Error OK");
    }
}
