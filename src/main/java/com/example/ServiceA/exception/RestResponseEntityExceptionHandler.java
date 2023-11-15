package com.example.ServiceA.exception;

import com.example.ServiceA.util.ColorLog;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
  @ExceptionHandler({
      HttpRequestMethodNotSupportedException.class,
      HttpMediaTypeNotSupportedException.class,
      HttpMediaTypeNotAcceptableException.class,
      MissingPathVariableException.class,
      MissingServletRequestParameterException.class,
      ServletRequestBindingException.class,
      ConversionNotSupportedException.class,
      TypeMismatchException.class,
      HttpMessageNotReadableException.class,
      HttpMessageNotWritableException.class,
      MethodArgumentNotValidException.class,
      MissingServletRequestPartException.class,
      BindException.class,
      NoHandlerFoundException.class,
      AsyncRequestTimeoutException.class
  })

  protected ResponseEntity<String> handleAllException(Exception exception) {
    log.warn(ColorLog.getWarn("AN ERROR HAS OCCURRED"));
    ColorLog.printStackTrace(exception);
    return ResponseEntity.internalServerError().body("Internal Error OK");
  }
}
