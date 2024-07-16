package com.pantrychef.backend.controllers;

import com.pantrychef.backend.dtos.AuthenticationRequest;
import com.pantrychef.backend.dtos.AuthenticationResponse;
import com.pantrychef.backend.dtos.RegistrationRequest;
import com.pantrychef.backend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Web API endpoints related to user authentication
 */
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    /**
     * Creates a new user
     * @param request Details of the user to create
     * @param response The response object with which will encapsulate the return
     * @return Either a successful response with copy of the created user
     *         and a set cookies header with jwt token or a negative response
     *         if the user could not be created
     */
    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.register(request, response));
    }

    /**
     * Authenticates an existing user
     * @param request Details of the user to authenticate
     * @param response The response object with which will encapsulate the return
     * @return Either a successful response with copy of the authenticated user
     *         and a set cookies header with jwt token or a negative response
     *         if the user could not be authenticated
     */
    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.authenticate(request, response));
    }
}
