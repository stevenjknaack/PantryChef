package com.pantrychef.backend.repositories;

import java.util.Optional;

import com.pantrychef.backend.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByRecipeId(Integer recipeId);
    
    public List<Review> findByAuthorUsername(String authorUsername);
}
