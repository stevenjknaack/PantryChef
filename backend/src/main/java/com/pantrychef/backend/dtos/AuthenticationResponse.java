package com.pantrychef.backend.dtos;

import com.pantrychef.backend.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The response to a user authentication event
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String msg;
    private User user;
    private long eat;
}
