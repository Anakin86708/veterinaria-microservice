package com.ariel.clienteService.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

public class ExceptionResponse {
    private final Timestamp timestamp;
    private final HttpStatus status;
    private final String error;
    private final String message;
    private final String path;

    public ExceptionResponse(HttpStatus status, String error, String message, String path) {
        this.timestamp = Timestamp.from(Instant.now());
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
