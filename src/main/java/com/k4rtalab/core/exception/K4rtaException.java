package com.k4rtalab.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class K4rtaException extends RuntimeException {
    public K4rtaException(String s) {
        super(s);
    }
}
