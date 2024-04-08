package com.pantrychef.backend.controllers;

import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping(path = "/login")
    public String handleLoginRequest(@RequestBody User user) {
        return "not logged in";
    }

    @PostMapping(path = "/logout")
    public String handleLogoutRequest(@RequestBody User user) {
        return "not logged out";
    }
}
