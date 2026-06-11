package com.k4rtalab.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends K4rtaException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}