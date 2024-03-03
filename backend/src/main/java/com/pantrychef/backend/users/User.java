package com.pantrychef.backend.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    private Integer id;

    private String username;
    private String email;
    private String hashedPassword;

    // recipes?
    // reviews?
    // Likes/Saves?
}
