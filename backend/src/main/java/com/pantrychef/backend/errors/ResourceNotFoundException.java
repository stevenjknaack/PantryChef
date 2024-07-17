package com.pantrychef.backend.errors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown when an entity thought to exist is not found in the database
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ResponseStatusException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "A resource you tried to access could not be found");
    }

    public ResourceNotFoundException(String msg) {
        super(HttpStatus.NOT_FOUND, msg);
    }
}
