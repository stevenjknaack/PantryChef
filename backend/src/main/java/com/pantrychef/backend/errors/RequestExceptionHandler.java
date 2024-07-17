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
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", "Access denied");
        body.put("status", HttpStatus.FORBIDDEN);

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", "Authentication Failed");
        body.put("status", HttpStatus.UNAUTHORIZED);

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleDataAccessException(
            DataAccessException ex, WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", "There was an error with your request");
        body.put("status", HttpStatus.BAD_REQUEST);

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(
//            MethodArgumentNotValidException ex, WebRequest request
//    ) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("msg", "There was an error with validating your request");
//        body.put("status", HttpStatus.BAD_REQUEST);
//
//        return handleExceptionInternal(ex, body,
//                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }

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

    @ExceptionHandler()
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("msg", "Something went wrong with the server");
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
