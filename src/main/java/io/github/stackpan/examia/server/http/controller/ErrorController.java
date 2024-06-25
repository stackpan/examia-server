package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.http.resource.ErrorResource;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        var resource = new ErrorResource<>(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resource);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getBindingResult().getAllErrors();

        Map<String, String> fieldErrors = new HashMap<>(errors.size());
        errors.forEach(error -> fieldErrors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        var resource = new ErrorResource<>(fieldErrors);

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.BAD_REQUEST);
    }
}
