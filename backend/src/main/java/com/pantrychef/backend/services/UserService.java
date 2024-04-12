package com.pantrychef.backend.services;

import com.pantrychef.backend.entities.users.User;
import com.pantrychef.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    public User getUserFromAuthHeader(String authHeader) {
        String jWTToken = jwtService.extractJWTToken(authHeader);
        String username = jwtService.extractUsername(jWTToken);
        return userRepository.findById(username).orElseThrow();
    }
}
