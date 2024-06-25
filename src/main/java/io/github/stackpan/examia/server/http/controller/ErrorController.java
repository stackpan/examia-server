package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.http.resource.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        var resource = new ErrorResource<>(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resource);
    }

}
