package com.pantrychef.backend.repositories;

import java.util.Optional;

import com.pantrychef.backend.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * For database interactions related to Reviews
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByRecipeId(Integer recipeId);

    public List<Review> findByRecipeIdAndRating(Integer recipeId, Integer rating);

    public List<Review> findByAuthorUsername(String authorUsername);
}
