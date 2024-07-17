package com.pantrychef.backend.errors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/**
 * Catches and handles exceptions during requests
 */
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler()
    public ResponseEntity<Object> handleCustomExceptions(
            ResponseStatusException ex, WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", ex.getReason());
        body.put("status", ex.getStatusCode());

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), ex.getStatusCode(), request);
    }
}
