package com.pantrychef.backend.repositories;

import com.pantrychef.backend.entities.Review;
import com.pantrychef.backend.entities.recipes.Recipe;
import com.pantrychef.backend.entities.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    private User author;
    private Recipe recipe;
    private Review review;

    @BeforeEach
    public void init() {
//        author = userRepository.save(
//                User.builder()
//                .email("email")
//                .username("username")
//                .password("password")
//                .build()
//        );
//
//        recipe = recipeRepository.save(
//                Recipe.builder()
//                .name("recipe")
//                .author(author)
//                .build()
//        );
//
//        review = Review.builder()
//                .review("review")
//                .rating(5)
//                .dateCreated("1")
//                .dateModified("2")
//                .recipe(recipe)
//                .author(author)
//                .build();
    }

    @Test
    public void save_ReturnsSavedReview() {
//        Review savedReview = reviewRepository.save(review);
//
//        Assertions.assertNotNull(savedReview);
//        Assertions.assertTrue(savedReview.getId() > 0);
    }
}
