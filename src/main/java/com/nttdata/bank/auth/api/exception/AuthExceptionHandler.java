package com.nttdata.bank.auth.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Turns validation and credential errors into JSON responses.
 */
@RestControllerAdvice
public class AuthExceptionHandler {

    /**
     * @param ex rejected credentials or duplicate username
     * @return HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorBody> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorBody(ex.getMessage()));
    }

    /**
     * @param ex bean validation failure
     * @return HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorBody(message));
    }

    /**
     * @param ex unexpected failure
     * @return HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> unexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorBody("Unexpected error"));
    }

    /**
     * @param message explanation safe to show to the caller
     */
    public record ErrorBody(String message) {
    }
}
