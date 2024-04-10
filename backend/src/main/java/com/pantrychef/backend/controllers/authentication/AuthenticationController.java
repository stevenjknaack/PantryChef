package com.pantrychef.backend.controllers.authentication;

import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.register(request, response));
    }

//    @PostMapping(path = "/login")
//    public ResponseEntity<String> authenticate(
//            @RequestBody AuthenticationRequest request,
//            HttpServletResponse response
//    ) {
//        AuthenticationResponse authResponse = authService.authenticate(request);
//        String jWTToken = authResponse.getJWTToken();
//        Cookie cookie = new Cookie("jwtToken", jWTToken);
//        cookie.setMaxAge(3600);
//        response.addCookie(cookie);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
//        return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
//    }
}
