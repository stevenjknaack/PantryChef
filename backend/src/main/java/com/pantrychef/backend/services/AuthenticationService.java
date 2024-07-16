package com.pantrychef.backend.services;

import com.pantrychef.backend.dtos.AuthenticationRequest;
import com.pantrychef.backend.dtos.AuthenticationResponse;
import com.pantrychef.backend.dtos.RegistrationRequest;
import com.pantrychef.backend.entities.users.Role;
import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.errors.InvalidRequestException;
import com.pantrychef.backend.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides business logic for dealing with authentication
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final static int maxJWTCookieAge = 3600 * 24;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jWTService;
    @Autowired
    private AuthenticationManager authManager;

    /**
     * Creates a new user
     * @param request Details of the user to create
     * @param response The response object with which will encapsulate the return
     * @return Either a successful response with copy of the created user
     *         and a set cookies header with jwt token or a negative response
     *         if the user could not be created
     */
    public AuthenticationResponse register(
            RegistrationRequest request,
            HttpServletResponse response
    ) {
        if (userRepository.existsById(request.getUsername()))
            throw new InvalidRequestException();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jWTToken = jWTService.generateToken(user);

        addJWTCookieToResponse(jWTToken, response);

        return AuthenticationResponse.builder()
                .user(user)
                .eat(1) //TODO figure out eat
                .msg("Successfully registered.")
                .build();
    }

    /**
     * Authenticates an existing user
     * @param request Details of the user to authenticate
     * @param response The response object with which will encapsulate the return
     * @return Either a successful response with copy of the authenticated user
     *         and a set cookies header with jwt token or a negative response
     *         if the user could not be authenticated
     */
    public AuthenticationResponse authenticate(
            AuthenticationRequest request,
            HttpServletResponse response
    ) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jWTToken = jWTService.generateToken(user);

        addJWTCookieToResponse(jWTToken, response);

        return AuthenticationResponse.builder()
                .user(user)
                .eat(1) //TODO
                .msg("Successfully logged in.")
                .build();
    }

    /**
     * Takes a jwt token and adds it to the response with a SET-COOKIE header
     * @param jWTToken The jwt token
     * @param response The response
     */
    private void addJWTCookieToResponse(String jWTToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", jWTToken);
        cookie.setMaxAge(maxJWTCookieAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}
