package com.ariel.animalService.exceptions;

public class ActiveForeignKeyException extends RuntimeException {
    public ActiveForeignKeyException(long id) {
        super(String.format("Unable to delete an entry with id [%d] that is referenced as foreing key", id));
    }
}
