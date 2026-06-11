package com.k4rtalab.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedActionException extends K4rtaException {

    public UnauthorizedActionException(String message) {
        super(message);
    }
}