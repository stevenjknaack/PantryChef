package com.pantrychef.backend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String msg) {
        super(msg);
    }
}
