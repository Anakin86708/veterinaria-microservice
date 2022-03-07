package com.ariel.clienteService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicateUniqueResourceException extends RuntimeException {
    public DuplicateUniqueResourceException(String message) {
        super(message);
    }
}
