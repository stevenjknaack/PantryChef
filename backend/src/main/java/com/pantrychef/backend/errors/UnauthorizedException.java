package com.pantrychef.backend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * When a request tries to access or modify data without proper authorization
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "You are not authorized to perform this action");
    }

    public UnauthorizedException(String msg) {
        super(HttpStatus.UNAUTHORIZED, msg);
    }
}
