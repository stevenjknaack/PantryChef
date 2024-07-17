package com.pantrychef.backend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Specifies an invalid http request was made
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException  extends ResponseStatusException {
    public InvalidRequestException() {
        super(HttpStatus.BAD_REQUEST, "Something was wrong with your request");
    }

    public InvalidRequestException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }
}
