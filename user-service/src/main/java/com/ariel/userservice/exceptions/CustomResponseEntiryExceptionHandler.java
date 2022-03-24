package com.ariel.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntiryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateUniqueResourceException.class)
    public final ResponseEntity<ExceptionResponse> handleDuplicateUniqueResourceException(Exception e, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ExceptionResponse response = new ExceptionResponse(httpStatus, "Unprocessable Entity", e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(Exception e, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ExceptionResponse response = new ExceptionResponse(httpStatus, "Resource not found", e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, httpStatus);
    }
}
