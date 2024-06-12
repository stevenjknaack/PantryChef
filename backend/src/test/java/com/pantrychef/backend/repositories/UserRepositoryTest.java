package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_ReturnsSavedUser() {
        User user = User.builder()
                .email("email")
                .username("username")
                .password("password")
                .build();
        User savedUser = userRepository.save(user);
        Assertions.assertNotNull(savedUser);
    }
}
