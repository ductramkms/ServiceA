package com.example.ServiceA.exception;

import com.example.ServiceA.util.ColorLog;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

  /**
   * Handle the others exception, that usually occurs in the internal system.
   *
   * @param exception exception
   * @return 500 status
   */
  @Counted(value = "exception.counter", description = "Exception Counter")
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<String> handleAllException(Exception exception) {
    log.warn(ColorLog.getWarn("AN ERROR HAS OCCURRED"));
    ColorLog.printStackTrace(exception);
    return ResponseEntity.internalServerError().body("Internal Error OK");
  }
}
