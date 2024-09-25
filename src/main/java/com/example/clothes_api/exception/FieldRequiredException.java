package com.example.clothes_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class FieldRequiredException extends RuntimeException {
    public FieldRequiredException(String message) {
        super(message);
    }
}
