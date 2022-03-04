package com.ariel.veterinarioService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ActiveForeignKeyException extends RuntimeException {
    public ActiveForeignKeyException(long id) {
        super(String.format("Unable to delete an entry with id [%d] that is referenced as foreing key", id));
    }
}
