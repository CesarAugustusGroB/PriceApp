package com.miempresa.priceapplication.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Request");  // Keep this consistent with your expectations
        response.put("message", "One or more fields are invalid");
        response.putAll(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePriceNotFoundException(PriceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPriceRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPriceRequestException(InvalidPriceRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Request");
        response.put("message", "Formato de fecha inválido. Por favor, usa el formato ISO 8601: YYYY-MM-DDTHH:MM:SS");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriceServiceException.class)
    public ResponseEntity<Map<String, String>> handlePriceServiceException(PriceServiceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Service Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error inesperado");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });

        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Request");
        response.put("message", "La solicitud contiene parámetros inválidos.");
        response.putAll(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
