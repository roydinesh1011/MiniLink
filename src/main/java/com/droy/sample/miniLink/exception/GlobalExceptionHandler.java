package com.droy.sample.miniLink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.InvalidUrlException;

import java.util.TooManyListenersException;

/**
 * Interceptor Class to handle the Exceptions
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is used for throwing the Invalid URL Exception when the URL validation is failed.
     * @param e
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleAuthenticationException(InvalidUrlException e) {
        // do what you want with e
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

    /**
     * This method is used for throwing the error when there is no record found for a given minilink URL
     * @param e
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException e) {
        // do what you want with e
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

    /**
     * This method is used for throwing the error when there are too many incoming requests
     * @param e
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleTooManyRequestException(TooManyListenersException e) {
        // do what you want with e
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

}
